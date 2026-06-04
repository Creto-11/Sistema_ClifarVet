package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.AppointmentRequestDto;
import com.puenteblanco.pb.dto.response.AppointmentBookingResponseDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.Pet;
import com.puenteblanco.pb.entity.Servicio;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.entity.Veterinario;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.PetRepository;
import com.puenteblanco.pb.repository.ServiceRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.repository.VeterinarioRepository;
import com.puenteblanco.pb.services.interfaces.AppointmentClientService;
import com.puenteblanco.pb.services.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AppointmentClientServiceImpl implements AppointmentClientService {

        private final CitaRepository citaRepository;
        private final ServiceRepository servicioRepository;
        private final VeterinarioRepository veterinarioRepository;
        private final UserRepository userRepository;
        private final PetRepository petRepository;
        private final EmailService emailService;

        private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
        private static final String NOMBRE_CLINICA = "Clínica Veterinaria Puente Blanco";

        @Override
        @Transactional
        public AppointmentBookingResponseDto bookAppointment(Authentication auth, AppointmentRequestDto dto) {
                String correo = auth.getName();

                User user = userRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Servicio servicio = servicioRepository.findById(dto.getServicioId())
                                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

                Veterinario veterinario = veterinarioRepository.findById(dto.getVeterinarioId())
                                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

                Pet pet = petRepository.findById(dto.getPetId())
                                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

                if (!correo.equalsIgnoreCase(pet.getOwnerEmail())) {
                        throw new RuntimeException("La mascota seleccionada no pertenece al usuario autenticado.");
                }

                Cita cita = Cita.builder()
                                .usuario(user)
                                .servicio(servicio)
                                .veterinario(veterinario)
                                .pet(pet)
                                .fecha(LocalDate.parse(dto.getFecha()))
                                .hora(LocalTime.parse(dto.getHora()))
                                .precioCobrado(servicio.getPrecioBase())
                                .estado("PENDIENTE_PAGO")
                                .cantidadReprogramaciones(0)
                                .build();

                Cita citaGuardada = citaRepository.save(cita);

                sendAppointmentCreatedEmail(citaGuardada);

                return new AppointmentBookingResponseDto(
                                citaGuardada.getId(),
                                citaGuardada.getEstado(),
                                citaGuardada.getPrecioCobrado(),
                                "Cita pendiente de pago. Complete el pago para confirmar la reserva.");
        }

        private void sendAppointmentCreatedEmail(Cita cita) {
                String mensaje = String.format(
                                "Estimado(a) %s,\n\n" +
                                                "Hemos recibido la solicitud de cita para su mascota %s.\n\n" +
                                                "Detalle de la cita:\n" +
                                                "Fecha: %s\n" +
                                                "Hora: %s\n" +
                                                "Servicio: %s\n" +
                                                "Veterinario: %s\n" +
                                                "Estado: Pendiente de pago\n\n" +
                                                "Para confirmar la reserva, complete el pago desde la plataforma.\n\n" +
                                                "Atentamente,\n%s",
                                getNombreCliente(cita),
                                getNombreMascota(cita),
                                formatFecha(cita),
                                formatHora(cita),
                                getServicio(cita),
                                getVeterinario(cita),
                                NOMBRE_CLINICA);

                sendSafe(cita.getUsuario().getCorreo(), "Solicitud de cita recibida - Puente Blanco", mensaje);
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

                String nombres = usuario.getNombres() != null ? usuario.getNombres() : "";
                String apellido = usuario.getApellidoPaterno() != null ? usuario.getApellidoPaterno() : "";

                String nombreCompleto = (nombres + " " + apellido).trim();
                return nombreCompleto.isBlank() ? "cliente" : nombreCompleto;
        }

        private String getNombreMascota(Cita cita) {
                return cita.getPet() != null && cita.getPet().getName() != null
                                ? cita.getPet().getName()
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