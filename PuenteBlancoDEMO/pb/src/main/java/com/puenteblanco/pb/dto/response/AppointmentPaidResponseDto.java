// AppointmentPaidResponseDto.java
package com.puenteblanco.pb.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AppointmentPaidResponseDto {
    private Long citaId;
    private String nombreMascota;
    private String razaMascota;
    private String tipoServicio;
    private BigDecimal monto;
    private String fechaCita;
    private String estado;
}
