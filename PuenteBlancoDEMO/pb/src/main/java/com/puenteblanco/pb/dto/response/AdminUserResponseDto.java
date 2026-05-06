package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponseDto {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private String rol; // nombre del rol (ej: "CLIENT")
    private Integer rolId; // id del rol (ej: 1)
    private Boolean estado;
    private String motivoDesactivacion; // <-- Agregado

}