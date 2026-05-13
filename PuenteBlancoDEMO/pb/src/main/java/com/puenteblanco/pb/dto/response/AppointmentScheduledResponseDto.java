package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentScheduledResponseDto {
    private Long citaId;
    private String nombreMascota;
    private String tipoServicio;
    private BigDecimal monto;
    private String fechaCita;
    private String estado;
    private String razaMascota;
}
