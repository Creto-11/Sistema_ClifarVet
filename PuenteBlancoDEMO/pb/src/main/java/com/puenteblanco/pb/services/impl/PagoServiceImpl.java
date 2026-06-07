package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.StripePaymentRequestDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.Pago;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.PagoRepository;
import com.puenteblanco.pb.services.interfaces.EmailService;
import com.puenteblanco.pb.services.interfaces.PagoService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

        @Value("${stripe.secret.key}")
        private String stripeSecretKey;

        private final PagoRepository pagoRepository;
        private final CitaRepository citaRepository;
        private final EmailService emailService;

        private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
        private static final String NOMBRE_CLINICA = "Clínica Veterinaria Puente Blanco";

        @PostConstruct
        public void init() {
                Stripe.apiKey = stripeSecretKey;
        }

        @Override
        public PaymentIntent processPayment(StripePaymentRequestDto request) throws StripeException {
                Cita cita = citaRepository.findById(request.getCitaId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Cita no encontrada con ID: " + request.getCitaId()));

                if (!"PENDIENTE_PAGO".equalsIgnoreCase(cita.getEstado())) {
                        throw new IllegalStateException("Solo se pueden pagar citas pendientes de pago.");
                }

                if (pagoRepository.existsByCitaId(cita.getId())) {
                        throw new IllegalStateException("Esta cita ya tiene un pago registrado.");
                }

                long montoCentavos = cita.getPrecioCobrado()
                                .multiply(BigDecimal.valueOf(100))
                                .longValue();

                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                                .setAmount(montoCentavos)
                                .setCurrency(request.getCurrency())
                                .setDescription(request.getDescription())
                                .setPaymentMethod(request.getPaymentMethodId())
                                .setConfirm(true)
                                .setAutomaticPaymentMethods(
                                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                                                .setEnabled(true)
                                                                .setAllowRedirects(
                                                                                PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                                                .build())
                                .build();

                PaymentIntent paymentIntent = PaymentIntent.create(params);

                Pago pago = Pago.builder()
                                .stripePaymentIntentId(paymentIntent.getId())
                                .descripcion(request.getDescription())
                                .monto(montoCentavos)
                                .moneda("PEN")
                                .estado("COMPLETADO")
                                .fechaPago(LocalDateTime.now())
                                .cita(cita)
                                .build();
                pagoRepository.save(pago);

                cita.setEstado("PROGRAMADA");
                citaRepository.save(cita);

                sendPaymentConfirmationEmail(cita);

                return paymentIntent;
        }

        private void sendPaymentConfirmationEmail(Cita cita) {
                String mensaje = String.format(
                                "Estimado(a) %s,\n\n" +
                                                "Su pago fue registrado correctamente y su cita quedó confirmada.\n\n" +
                                                "Detalle de la cita:\n" +
                                                "Mascota: %s\n" +
                                                "Servicio: %s\n" +
                                                "Fecha: %s\n" +
                                                "Hora: %s\n" +
                                                "Veterinario: %s\n" +
                                                "Estado: Programada\n\n" +
                                                "Gracias por elegirnos.\n\n" +
                                                "Atentamente,\n%s",
                                getNombreCliente(cita),
                                getNombreMascota(cita),
                                getServicio(cita),
                                formatFecha(cita),
                                formatHora(cita),
                                getVeterinario(cita),
                                NOMBRE_CLINICA);

                sendSafe(cita.getUsuario().getCorreo(), "Cita confirmada - Puente Blanco", mensaje);
        }

        private void sendSafe(String to, String subject, String message) {
                try {
                        emailService.sendEmail(to, subject, message);
                } catch (Exception e) {
                        System.err.println("No se pudo enviar correo a " + to + ": " + e.getMessage());
                }
        }

        private String getNombreCliente(Cita cita) {
                User usuario = cita.getUsuario();
                if (usuario == null) {
                        return "cliente";
                }
                return (usuario.getNombres() + " " + usuario.getApellidoPaterno()).trim();
        }

        private String getNombreMascota(Cita cita) {
                return cita.getPet() != null && cita.getPet().getName() != null ? cita.getPet().getName()
                                : "su mascota";
        }

        private String getServicio(Cita cita) {
                return cita.getServicio() != null && cita.getServicio().getDescripcion() != null
                                ? cita.getServicio().getDescripcion()
                                : "Servicio veterinario";
        }

        private String getVeterinario(Cita cita) {
                String nombre = cita.getVeterinario() != null ? cita.getVeterinario().getNombreCompleto() : null;
                return nombre != null && !nombre.isBlank() ? nombre : "veterinario asignado";
        }

        private String formatFecha(Cita cita) {
                return cita.getFecha() != null ? cita.getFecha().format(DATE_FORMAT) : "fecha no registrada";
        }

        private String formatHora(Cita cita) {
                return cita.getHora() != null ? cita.getHora().format(TIME_FORMAT) : "hora no registrada";
        }
}