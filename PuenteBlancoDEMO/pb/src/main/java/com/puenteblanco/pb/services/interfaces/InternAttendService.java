package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.request.InternAttendRequestDto;

public interface InternAttendService {
    void registrarAtencionMedica(Long citaId, InternAttendRequestDto dto, String emailInterno);
}
