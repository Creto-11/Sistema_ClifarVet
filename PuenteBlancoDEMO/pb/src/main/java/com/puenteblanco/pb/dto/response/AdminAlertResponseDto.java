package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAlertResponseDto {
    private String tipo;         // "info", "warning", "danger"
    private String titulo;       // Ej: "Citas sin atención médica"
    private String descripcion;  // Ej: "3 citas completadas sin atención médica registrada"
}
