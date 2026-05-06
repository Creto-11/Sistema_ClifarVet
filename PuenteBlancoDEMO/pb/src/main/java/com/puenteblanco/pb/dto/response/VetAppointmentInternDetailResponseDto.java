package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetAppointmentInternDetailResponseDto {

    private Long citaId;

    // Información de la atención médica
    private String observacionesClinicas;
    private String diagnostico;
    private String tratamiento;
    private String prescripcion;

    // Información de la mascota
    private String nombreMascota;
    private String tipoMascota;
    private String razaMascota;
    private Integer edadMascota;

    // Cliente
    private String nombreCliente;

    // Veterinario a cargo
    private String veterinario;

    // Interno que registró
    private String practicante;

    // Servicio
    private String servicio;
    private String fecha;
    private String hora;
}