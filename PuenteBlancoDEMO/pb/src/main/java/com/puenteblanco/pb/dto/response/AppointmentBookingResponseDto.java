package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AppointmentBookingResponseDto {
    private Long citaId;
    private String estado;
    private BigDecimal monto;
    private String mensaje;
}