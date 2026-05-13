// VetClinicalRecordServiceImpl.java
package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.AttendAppointmentRequestDto;
import com.puenteblanco.pb.dto.response.AppointmentDetailResponseDto;
import com.puenteblanco.pb.entity.AtencionMedica;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.repository.AtencionMedicaRepository;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.services.interfaces.VetClinicalRecordService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VetClinicalRecordServiceImpl implements VetClinicalRecordService {

    private final CitaRepository citaRepository;
    private final AtencionMedicaRepository atencionMedicaRepository;

    @Override
    public AppointmentDetailResponseDto getAppointmentDetails(Long citaId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada"));

        return AppointmentDetailResponseDto.builder()
                .citaId(cita.getId())
                .hora(cita.getHora().toString())
                .fecha(cita.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .nombreCliente(cita.getUsuario().getNombres())
                .emailCliente(cita.getUsuario().getCorreo())
                .nombreMascota(cita.getPet().getName())
                .tipoMascota(cita.getPet().getType())
                .razaMascota(cita.getPet().getBreed())
                .edadMascota(cita.getPet().getAge())
                .servicio(cita.getServicio().getDescripcion())
                .build();
    }

    @Override
    public void saveMedicalAttention(Long citaId, AttendAppointmentRequestDto dto) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada"));

        Optional<AtencionMedica> optionalAtencion = atencionMedicaRepository.findByCita_Id(citaId);

        AtencionMedica atencion;

        if (optionalAtencion.isPresent()) {
            // Actualizar atención existente
            atencion = optionalAtencion.get();
            atencion.setObservacionesClinicas(dto.getObservacionesClinicas());
            atencion.setDiagnostico(dto.getDiagnostico());
            atencion.setTratamiento(dto.getTratamiento());
            atencion.setPrescripcion(dto.getPrescripcion());
            atencion.setEstadoValidacion("COMPLETADA");
            atencion.setRegistradaPorId(cita.getVeterinario().getUsuario().getId());
            atencion.setActivo(true);
            
        } else {
            // Crear nueva atención
            atencion = AtencionMedica.builder()
                    .cita(cita)
                    .observacionesClinicas(dto.getObservacionesClinicas())
                    .diagnostico(dto.getDiagnostico())
                    .tratamiento(dto.getTratamiento())
                    .prescripcion(dto.getPrescripcion())
                    .estadoValidacion("COMPLETADA")
                    .registradaPorId(cita.getVeterinario().getUsuario().getId())
                    .activo(true)
                    .build();
        }

        atencionMedicaRepository.save(atencion);

        cita.setEstado("COMPLETADA");
        citaRepository.save(cita);
    }
}
