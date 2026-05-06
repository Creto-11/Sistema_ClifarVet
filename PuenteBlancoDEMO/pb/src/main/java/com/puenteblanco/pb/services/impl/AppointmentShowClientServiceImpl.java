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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentShowClientServiceImpl implements AppointmentShowClientService {

    private final CitaRepository citaRepository;
    private final UserRepository userRepository;

    @Override
    public List<AppointmentListResponseDto> getAppointmentsByClient(Authentication auth) {
        String correo = auth.getName();
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return citaRepository.findByUsuario(user).stream()
                .map(c -> new AppointmentListResponseDto(
                        c.getServicio().getDescripcion(),
                        c.getVeterinario().getUsuario().getNombres(),
                        c.getPet().getName(),
                        c.getFecha().toString(),
                        c.getHora().toString(),
                        c.getEstado(),
                        c.getMotivoCancelacion()  // <- ahora se incluye el motivo
                ))
                .collect(Collectors.toList());
    }
}

