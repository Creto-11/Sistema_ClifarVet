package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMonthlyRevenueResponseDto {
    private String mes;      // Ejemplo: "Enero"
    private double monto;    // Ejemplo: 3200.00
}