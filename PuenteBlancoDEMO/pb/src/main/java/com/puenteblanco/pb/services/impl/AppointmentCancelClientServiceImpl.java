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
import org.springframework.transaction.annotation.Transactional;

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
                .filter(this::isEstadoReprogramable)
                .filter(c -> c.getCantidadReprogramaciones() == null || c.getCantidadReprogramaciones() == 0)
                .map(c -> new AppointmentCancelOptionDto(
                        c.getId(),
                        c.getFecha().toString(),
                        c.getHora().toString(),
                        c.getVeterinario().getUsuario().getNombres(),
                        c.getServicio().getDescripcion()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void rescheduleAppointment(Long id, AppointmentRescheduleRequestDto dto, Authentication auth) {
        String correo = auth.getName();

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!cita.getUsuario().getCorreo().equalsIgnoreCase(correo)) {
            throw new RuntimeException("No autorizado para reprogramar esta cita.");
        }

        if (!isEstadoReprogramable(cita)) {
            throw new RuntimeException("Solo se pueden reprogramar citas programadas o pagadas.");
        }

        if (cita.getCantidadReprogramaciones() != null && cita.getCantidadReprogramaciones() >= 1) {
            throw new RuntimeException(
                    "Esta cita ya fue reprogramada una vez. No se permite una segunda reprogramación.");
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
        cita.setCantidadReprogramaciones(1);
        cita.setEstado("REPROGRAMADA");

        citaRepository.save(cita);
    }

    private boolean isEstadoReprogramable(Cita cita) {
        String estado = cita.getEstado() != null ? cita.getEstado().toUpperCase() : "";
        return "PROGRAMADA".equals(estado) || "PAGADA".equals(estado);
    }
}