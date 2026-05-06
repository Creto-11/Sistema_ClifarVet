package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.response.AppointmentPaidResponseDto;
import com.puenteblanco.pb.dto.response.AppointmentScheduledResponseDto;

import java.util.List;

public interface CitaClienteService {
    List<AppointmentScheduledResponseDto> obtenerCitasProgramadasPorUsuario(String correoUsuario);
    List<AppointmentPaidResponseDto> obtenerCitasPagadasPorUsuario(String correo);
}
