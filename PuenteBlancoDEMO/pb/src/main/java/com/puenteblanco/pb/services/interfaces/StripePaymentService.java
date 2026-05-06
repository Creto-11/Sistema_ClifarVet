package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.entity.Pago;
import com.puenteblanco.pb.entity.Cita;
import com.stripe.model.PaymentIntent;

public interface StripePaymentService {
    Pago guardarPago(PaymentIntent paymentIntent, Cita cita);
}