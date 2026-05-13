package com.puenteblanco.pb.dto.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioUpdateRequest {
    private String diaSemana; 
    private LocalTime horaComienzo;
    private LocalTime horaFin;
}
