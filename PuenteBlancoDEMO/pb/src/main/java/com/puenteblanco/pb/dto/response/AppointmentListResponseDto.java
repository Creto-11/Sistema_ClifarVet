package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentListResponseDto {
    private String servicio;
    private String veterinario;
    private String mascota;
    private String fecha; 
    private String hora;  
    private String estado;
    private String motivoCancelacion; 
}
