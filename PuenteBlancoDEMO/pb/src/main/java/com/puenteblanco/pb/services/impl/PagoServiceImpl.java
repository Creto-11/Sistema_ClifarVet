// PagoServiceImpl.java
package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.StripePaymentRequestDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.Pago;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.PagoRepository;
import com.puenteblanco.pb.services.interfaces.PagoService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public PaymentIntent processPayment(StripePaymentRequestDto request) throws StripeException {
        // Validar cita
        Cita cita = citaRepository.findById(request.getCitaId())
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + request.getCitaId()));

        if (!"completada".equalsIgnoreCase(cita.getEstado())) {
            throw new IllegalStateException("Solo se pueden pagar citas con estado 'completada'.");
        }

        if (pagoRepository.existsByCitaId(cita.getId())) {
            throw new IllegalStateException("Esta cita ya tiene un pago registrado.");
        }

        // Crear intento de pago en Stripe
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(cita.getPrecioCobrado().multiply(BigDecimal.valueOf(100)).longValue())
                .setCurrency(request.getCurrency())
                .setDescription(request.getDescription())
                .setPaymentMethod(request.getPaymentMethodId())
                .setConfirm(true)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Guardar en base de datos
        Pago pago = Pago.builder()
        .stripePaymentIntentId(paymentIntent.getId())
        .descripcion(request.getDescription())
        .monto(request.getAmount())
        .moneda("PEN")
        .fechaPago(LocalDateTime.now())
        .cita(cita)
        .build();

        pagoRepository.save(pago);

        // Actualizar cita
        cita.setEstado("PAGADA");
        citaRepository.save(cita);

        return paymentIntent;
    }
}
