package com.puenteblanco.pb.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class CitaCanceladaDTO {
    private Long citaId;
    private String cliente;
    private String mascota;
    private String servicio;
    private String veterinario;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivoCancelacion;
}