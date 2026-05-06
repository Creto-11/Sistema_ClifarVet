// PaymentRequestDto.java
package com.puenteblanco.pb.dto.request;

import lombok.Data;

@Data
public class StripePaymentRequestDto {
    private String paymentMethodId; // ID generado por Stripe (ej. pm_xxx)
    private Long amount;            // Monto en centavos (ej. 1500 = S/15.00)
    private String currency;        // "usd" o "pen"
    private String description;     // Ej. "Pago por consulta veterinaria"
    private Long citaId;
}

