package com.puenteblanco.pb.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class CitaPorMascotaDTO {
    private String nombreMascota;
    private String tipoMascota;       // Ej: Perro, Gato
    private String raza;
    private String servicio;
    private String cliente;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
}