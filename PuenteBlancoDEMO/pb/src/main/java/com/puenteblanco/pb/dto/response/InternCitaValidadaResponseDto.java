package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternCitaValidadaResponseDto {
    private String fechaHora;     // Ej: "2025-07-01 14:40"
    private String mascota;       // Ej: "Luna"
    private String servicio;      // Ej: "Vacuna antirrábica"
    private String veterinario;   // Ej: "Dra. Kiara"
    private String estado;        // Ej: "PAGADA"
    private String cliente;       // Ej: "Juan Pérez"
}