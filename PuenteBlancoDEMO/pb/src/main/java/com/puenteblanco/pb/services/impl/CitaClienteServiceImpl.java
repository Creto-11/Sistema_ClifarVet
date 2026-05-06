package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.response.AppointmentPaidResponseDto;
import com.puenteblanco.pb.dto.response.AppointmentScheduledResponseDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.services.interfaces.CitaClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaClienteServiceImpl implements CitaClienteService {

    private final CitaRepository citaRepository;

    @Override
    public List<AppointmentScheduledResponseDto> obtenerCitasProgramadasPorUsuario(String correoUsuario) {
        List<Cita> citas = citaRepository.findByUsuarioCorreoAndEstadoIgnoreCase(correoUsuario, "COMPLETADA");

        return citas.stream().map(cita ->
                AppointmentScheduledResponseDto.builder()
                        .citaId(cita.getId())
                        .nombreMascota(cita.getPet().getName())
                        .razaMascota(cita.getPet().getBreed())
                        .tipoServicio(cita.getServicio().getDescripcion())
                        .monto(cita.getPrecioCobrado())
                        .fechaCita(cita.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .estado(cita.getEstado())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
public List<AppointmentPaidResponseDto> obtenerCitasPagadasPorUsuario(String correo) {
    List<Cita> citas = citaRepository.findAllByUsuarioCorreoAndEstado(correo, "pagada");

    return citas.stream()
            .map(cita -> AppointmentPaidResponseDto.builder()
                    .citaId(cita.getId())
                    .nombreMascota(cita.getPet().getName())
                    .razaMascota(cita.getPet().getBreed())
                    .tipoServicio(cita.getServicio().getDescripcion())
                    .monto(cita.getPrecioCobrado())
                    .fechaCita(cita.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .estado(cita.getEstado())
                    .build())
            .collect(Collectors.toList());
}
}
