package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternAppointmentResponseDto {

    private Long citaId;

    // Cliente
    private String nombreCliente;

    // Mascota
    private String nombreMascota;
    private String tipoMascota;
    private String razaMascota;
    private Integer edadMascota;

    // Servicio
    private String servicio;
    private String fecha;
    private String hora;

    // Veterinario responsable
    private String veterinarioACargo;
    private String estado;
}