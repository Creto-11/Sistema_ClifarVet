package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.response.VetAppointmentInternDetailResponseDto;
import com.puenteblanco.pb.dto.response.VetAppointmentResponseDto;
import com.puenteblanco.pb.dto.response.VetAppointmentValidateResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface VetAppointmentService {
    List<VetAppointmentResponseDto> getAppointmentsForDate(LocalDate date);
    List<VetAppointmentValidateResponseDto> getCitasEvaluadasPendientesValidacion();
    VetAppointmentInternDetailResponseDto getDetalleEvaluacion(Long citaId);
    void actualizarEstadoCita(Long citaId, String nuevoEstado);

}
