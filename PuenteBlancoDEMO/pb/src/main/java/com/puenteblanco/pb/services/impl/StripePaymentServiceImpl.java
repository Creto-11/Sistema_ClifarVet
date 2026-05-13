package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.Pago;
import com.puenteblanco.pb.repository.PagoRepository;
import com.puenteblanco.pb.services.interfaces.PagoService;
import com.puenteblanco.pb.services.interfaces.StripePaymentService;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StripePaymentServiceImpl implements StripePaymentService {

    private final PagoRepository pagoRepository;

    @Override
    public Pago guardarPago(PaymentIntent paymentIntent, Cita cita) {
        Pago pago = Pago.builder()
                .cita(cita)
                .monto(paymentIntent.getAmount())
                .moneda(paymentIntent.getCurrency())
                .descripcion(paymentIntent.getDescription())
                .estado(paymentIntent.getStatus())
                .stripePaymentIntentId(paymentIntent.getId())
                .fechaPago(LocalDateTime.now())
                .build();

        return pagoRepository.save(pago);
    }
}