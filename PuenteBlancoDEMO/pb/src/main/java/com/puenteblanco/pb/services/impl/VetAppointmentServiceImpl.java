package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.response.VetAppointmentInternDetailResponseDto;
import com.puenteblanco.pb.dto.response.VetAppointmentResponseDto;
import com.puenteblanco.pb.dto.response.VetAppointmentValidateResponseDto;
import com.puenteblanco.pb.entity.AtencionMedica;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.entity.Veterinario;
import com.puenteblanco.pb.exception.ResourceNotFoundException;
import com.puenteblanco.pb.repository.AtencionMedicaRepository;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.security.AuthUtils;
import com.puenteblanco.pb.services.interfaces.VetAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VetAppointmentServiceImpl implements VetAppointmentService {

    private final CitaRepository citaRepository;
    private final UserRepository userRepository;
    private final AtencionMedicaRepository atencionMedicaRepository;

    @Override
    public List<VetAppointmentResponseDto> getAppointmentsForDate(LocalDate date) {
        String correo = AuthUtils.getAuthenticatedEmail();
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Veterinario vet = user.getVeterinario();
        if (vet == null)
            throw new RuntimeException("Usuario no es veterinario");

        List<Cita> citas = citaRepository.findByVeterinarioIdAndFechaAndEstadoIn(
                vet.getId(),
                date,
                Arrays.asList("PROGRAMADA", "PAGADA", "REPROGRAMADA", "DERIVADA", "VALIDADA", "COMPLETADA"));

        return citas.stream().map(cita -> {
            String nombreCliente = cita.getUsuario().getNombres() + " " + cita.getUsuario().getApellidoPaterno();
            String nombreMascota = cita.getPet() != null ? cita.getPet().getName() : "(No registrada)";
            String razaMascota = cita.getPet() != null ? cita.getPet().getBreed() : "";
            String nombreServicio = cita.getServicio().getDescripcion();
            String comentarios = "";

            return new VetAppointmentResponseDto(
                    cita.getId(),
                    cita.getHora().toString(),
                    nombreCliente,
                    nombreMascota,
                    razaMascota,
                    nombreServicio,
                    comentarios,
                    cita.getEstado());
        }).collect(Collectors.toList());
    }

    @Override
    public List<VetAppointmentValidateResponseDto> getCitasEvaluadasPendientesValidacion() {
        List<Cita> citas = citaRepository.findByEstado("EVALUADA");

        return citas.stream().map(cita -> VetAppointmentValidateResponseDto.builder()
                .citaId(cita.getId())
                .nombreCliente(cita.getUsuario().getNombres())
                .nombreMascota(cita.getPet().getName())
                .tipoMascota(cita.getPet().getType())
                .razaMascota(cita.getPet().getBreed())
                .servicio(cita.getServicio().getDescripcion())
                .fecha(cita.getFecha().toString())
                .hora(cita.getHora().toString())
                .nombrePracticante(cita.getIntern().getNombres())
                .build()).collect(Collectors.toList());
    }

    @Override
    public VetAppointmentInternDetailResponseDto getDetalleEvaluacion(Long citaId) {
        AtencionMedica atencion = atencionMedicaRepository.findByCita_Id(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Atención médica no encontrada para esta cita"));

        Cita cita = atencion.getCita();

        return VetAppointmentInternDetailResponseDto.builder()
                .citaId(cita.getId())
                .observacionesClinicas(atencion.getObservacionesClinicas())
                .diagnostico(atencion.getDiagnostico())
                .tratamiento(atencion.getTratamiento())
                .prescripcion(atencion.getPrescripcion())
                .build();
    }

    @Override
    public void actualizarEstadoCita(Long citaId, String nuevoEstado) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + citaId));

        cita.setEstado(nuevoEstado);
        citaRepository.save(cita);

        atencionMedicaRepository.findByCita_Id(citaId).ifPresent(atencion -> {
            if ("COMPLETADA".equalsIgnoreCase(nuevoEstado) || "VALIDADA".equalsIgnoreCase(nuevoEstado)) {
                atencion.setEstadoValidacion("COMPLETADA");
                atencion.setActivo(true);
            } else if ("PROGRAMADA".equalsIgnoreCase(nuevoEstado)) {
                atencion.setEstadoValidacion("RECHAZADA");
                atencion.setActivo(false);
            }
            atencionMedicaRepository.save(atencion);
        });
    }
}
