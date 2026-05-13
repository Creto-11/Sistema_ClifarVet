package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.request.AdminUpdateAppointmentRequestDto;
import com.puenteblanco.pb.dto.response.AdminAppointmentResponseDto;

import java.util.List;

public interface AdminAppointmentService {
    List<AdminAppointmentResponseDto> listarCitas();
    void actualizarCita(Long id, AdminUpdateAppointmentRequestDto dto);
}
