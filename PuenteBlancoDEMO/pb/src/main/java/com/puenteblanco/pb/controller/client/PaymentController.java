package com.puenteblanco.pb.controller.client;

import com.puenteblanco.pb.dto.request.StripePaymentRequestDto;
import com.puenteblanco.pb.dto.response.StripePaymentResponseDto;
import com.puenteblanco.pb.services.interfaces.PagoService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PagoService paymentService;

    public PaymentController(PagoService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
public ResponseEntity<?> createPayment(@RequestBody StripePaymentRequestDto paymentRequestDto) {
    try {
        PaymentIntent intent = paymentService.processPayment(paymentRequestDto);
        return ResponseEntity.ok(new StripePaymentResponseDto(
                intent.getId(),
                intent.getStatus()
        ));
    } catch (StripeException e) {
        return ResponseEntity.status(500).body("Error procesando el pago: " + e.getMessage());
    }
}

}

