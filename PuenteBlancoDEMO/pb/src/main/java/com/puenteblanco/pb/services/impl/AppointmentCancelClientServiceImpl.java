package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.AppointmentRescheduleRequestDto;
import com.puenteblanco.pb.dto.response.AppointmentCancelOptionDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.services.interfaces.AppointmentCancelClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentCancelClientServiceImpl implements AppointmentCancelClientService {

    private final CitaRepository citaRepository;
    private final UserRepository userRepository;

    @Override
    public List<AppointmentCancelOptionDto> getReschedulableAppointments(Authentication auth) {
        String correo = auth.getName();

        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return citaRepository.findByUsuario(user).stream()
                .filter(c -> "PROGRAMADA".equalsIgnoreCase(c.getEstado()) || "PAGADA".equalsIgnoreCase(c.getEstado()))
                .map(c -> new AppointmentCancelOptionDto(
                        c.getId(),
                        c.getFecha().toString(),
                        c.getHora().toString(),
                        c.getVeterinario().getUsuario().getNombres(),
                        c.getServicio().getDescripcion()))
                .collect(Collectors.toList());
    }

    @Override
    public void rescheduleAppointment(Long id, AppointmentRescheduleRequestDto dto, Authentication auth) {
        String correo = auth.getName();

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!cita.getUsuario().getCorreo().equals(correo)) {
            throw new RuntimeException("No autorizado para reprogramar esta cita.");
        }

        if (!"PROGRAMADA".equalsIgnoreCase(cita.getEstado()) && !"PAGADA".equalsIgnoreCase(cita.getEstado())) {
            throw new RuntimeException("Solo se pueden reprogramar citas activas.");
        }

        if (dto.getMotivoReprogramacion() == null || dto.getMotivoReprogramacion().trim().isEmpty()) {
            throw new RuntimeException("Debe ingresar el motivo de reprogramación.");
        }

        LocalDate nuevaFecha = LocalDate.parse(dto.getFecha());
        LocalTime nuevaHora = LocalTime.parse(dto.getHora());

        LocalDateTime nuevaFechaHora = LocalDateTime.of(nuevaFecha, nuevaHora);

        if (nuevaFechaHora.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede reprogramar a una fecha u hora pasada.");
        }

        long cruces = citaRepository.countActiveAppointmentsAtSameSlot(
                cita.getVeterinario().getId(),
                nuevaFecha,
                nuevaHora,
                cita.getId());

        if (cruces > 0) {
            throw new RuntimeException("El horario seleccionado ya está ocupado.");
        }

        if (cita.getFechaOriginal() == null) {
            cita.setFechaOriginal(cita.getFecha());
        }

        if (cita.getHoraOriginal() == null) {
            cita.setHoraOriginal(cita.getHora());
        }

        cita.setFecha(nuevaFecha);
        cita.setHora(nuevaHora);
        cita.setMotivoReprogramacion(dto.getMotivoReprogramacion().trim());
        cita.setCantidadReprogramaciones(
                cita.getCantidadReprogramaciones() == null ? 1 : cita.getCantidadReprogramaciones() + 1);

        citaRepository.save(cita);
    }
}