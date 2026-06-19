package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripePaymentResponseDto {
    private String paymentIntentId;
    private String status;
}
