package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.request.HorarioUpdateRequest;

import java.util.List;

public interface VetHorarioUpdateService {
    void actualizarHorariosSemanaSiguiente(List<HorarioUpdateRequest> nuevosHorarios, String correoVeterinario);
}