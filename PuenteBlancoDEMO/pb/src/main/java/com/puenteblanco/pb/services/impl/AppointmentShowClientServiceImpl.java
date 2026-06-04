package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.response.AppointmentListResponseDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.services.interfaces.AppointmentShowClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentShowClientServiceImpl implements AppointmentShowClientService {

        private final CitaRepository citaRepository;
        private final UserRepository userRepository;

        private static final Set<String> ESTADOS_VISIBLES_CLIENTE = Set.of(
                        "PROGRAMADA",
                        "PAGADA",
                        "REPROGRAMADA",
                        "DERIVADA",
                        "VALIDADA",
                        "COMPLETADA");

        @Override
        public List<AppointmentListResponseDto> getAppointmentsByClient(Authentication auth) {
                String correo = auth.getName();

                User user = userRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                return citaRepository.findByUsuario(user).stream()
                                .filter(cita -> cita.getEstado() != null)
                                .filter(cita -> ESTADOS_VISIBLES_CLIENTE.contains(cita.getEstado().toUpperCase()))
                                .map(c -> new AppointmentListResponseDto(
                                                c.getId(),
                                                c.getServicio().getDescripcion(),
                                                c.getVeterinario().getId(),
                                                c.getVeterinario().getUsuario().getNombres(),
                                                c.getPet().getName(),
                                                c.getFecha().toString(),
                                                c.getHora().toString(),
                                                c.getEstado(),
                                                c.getMotivoReprogramacion()))
                                .collect(Collectors.toList());
        }
}
