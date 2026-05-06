package com.puenteblanco.pb.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdminPaymentResponseDto {
    private LocalDate fechaPago;
    private String cliente;
    private String servicio;
    private BigDecimal monto; // ← CAMBIO AQUÍ
    private String metodo;
    private String estado;

    public AdminPaymentResponseDto(LocalDate fechaPago, String cliente, String servicio,
                                   BigDecimal monto, String metodo, String estado) {
        this.fechaPago = fechaPago;
        this.cliente = cliente;
        this.servicio = servicio;
        this.monto = monto;
        this.metodo = metodo;
        this.estado = estado;
    }
}