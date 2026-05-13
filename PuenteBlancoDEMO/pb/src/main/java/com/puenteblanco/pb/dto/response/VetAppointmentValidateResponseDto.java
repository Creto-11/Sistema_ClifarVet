// EvaluatedAppointmentResponseDto.java
package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetAppointmentValidateResponseDto {
    private Long citaId;

    // Cliente
    private String nombreCliente;

    // Mascota
    private String nombreMascota;
    private String tipoMascota;
    private String razaMascota;

    // Servicio
    private String servicio;

    // Fecha y hora
    private String fecha;
    private String hora;

    // Interno que hizo la atención
    private String nombrePracticante;
}
