package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.response.InternAppointmentResponseDto;
import com.puenteblanco.pb.dto.response.InternCitaValidadaResponseDto;

import java.util.List;

public interface InternAppointmentService {
    List<InternAppointmentResponseDto> getCitasDerivadasDelInterno(Long internId);
    void marcarCitasDerivadasComoVistas(Long internId); 
    List<InternCitaValidadaResponseDto> getCitasValidadasPorIntern(Long internId);
    List<InternAppointmentResponseDto> getCitasDerivadasNoVistas(Long internId);

}
