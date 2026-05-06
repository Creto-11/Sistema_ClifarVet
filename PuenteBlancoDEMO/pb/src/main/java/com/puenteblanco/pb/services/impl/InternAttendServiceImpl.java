package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.InternAttendRequestDto;
import com.puenteblanco.pb.entity.AtencionMedica;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.exception.ResourceNotFoundException;
import com.puenteblanco.pb.repository.AtencionMedicaRepository;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.services.interfaces.InternAttendService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternAttendServiceImpl implements InternAttendService {

    private final CitaRepository citaRepository;
    private final AtencionMedicaRepository atencionMedicaRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void registrarAtencionMedica(Long citaId, InternAttendRequestDto dto, String emailInterno) {
        // Buscar la cita
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + citaId));

        if (!"DERIVADA".equals(cita.getEstado())) {
            throw new IllegalStateException("Solo se puede registrar atención médica para citas derivadas.");
        }

        // Buscar el ID del usuario practicante a partir del correo
        User practicante = userRepository.findByCorreo(emailInterno)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + emailInterno));

        // Crear la atención médica
        AtencionMedica atencion = new AtencionMedica();
        atencion.setCita(cita);
        atencion.setObservacionesClinicas(dto.getObservacionesClinicas());
        atencion.setDiagnostico(dto.getDiagnostico());
        atencion.setTratamiento(dto.getTratamiento());
        atencion.setPrescripcion(dto.getPrescripcion());
        atencion.setRegistradaPorId(practicante.getId()); // ← Asigna ID directamente
        atencion.setEstadoValidacion("EVALUADA");
        atencion.setActivo(true);

        atencionMedicaRepository.save(atencion);

        // Actualizar el estado de la cita
        cita.setEstado("EVALUADA");
        citaRepository.save(cita);
    }
}