package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.services.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentReminderService {

    private final CitaRepository citaRepository;
    private final EmailService emailService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final String NOMBRE_CLINICA = "Clínica Veterinaria Puente Blanco";

    @Scheduled(cron = "0 * * * * ?")
    public void enviarRecordatorioTreintaMinutosAntes() {
        LocalDate hoy = LocalDate.now();
        LocalTime horaObjetivo = LocalTime.now()
                .plusMinutes(30)
                .truncatedTo(ChronoUnit.MINUTES);

        List<Cita> citas = citaRepository.findCitasConfirmadasForReminderAt(hoy, horaObjetivo);

        for (Cita cita : citas) {
            sendReminderEmail(
                    cita,
                    "Recordatorio de cita - Puente Blanco",
                    "Le recordamos que su cita está programada dentro de 30 minutos.");
        }
    }

    @Scheduled(cron = "0 * * * * ?")
    public void enviarRecordatorioDiezMinutosAntes() {
        LocalDate hoy = LocalDate.now();
        LocalTime horaObjetivo = LocalTime.now()
                .plusMinutes(10)
                .truncatedTo(ChronoUnit.MINUTES);

        List<Cita> citas = citaRepository.findCitasConfirmadasForReminderAt(hoy, horaObjetivo);

        for (Cita cita : citas) {
            sendReminderEmail(
                    cita,
                    "Recordatorio final de cita - Puente Blanco",
                    "Su cita está programada dentro de 10 minutos.");
        }
    }

    private void sendReminderEmail(Cita cita, String subject, String encabezado) {
        String estado = cita.getEstado() != null ? cita.getEstado().toUpperCase() : "";

        if (!"PROGRAMADA".equals(estado) && !"REPROGRAMADA".equals(estado)) {
            return;
        }

        String mensaje = String.format(
                "Estimado(a) %s,\n\n" +
                        "%s\n\n" +
                        "Detalle de la cita:\n" +
                        "Mascota: %s\n" +
                        "Servicio: %s\n" +
                        "Fecha: %s\n" +
                        "Hora: %s\n" +
                        "Veterinario: %s\n" +
                        "Estado: %s\n\n" +
                        "Por favor, procure llegar unos minutos antes de la hora programada.\n\n" +
                        "Atentamente,\n%s",
                getNombreCliente(cita),
                encabezado,
                getNombreMascota(cita),
                getServicio(cita),
                formatFecha(cita),
                formatHora(cita),
                getVeterinario(cita),
                estado,
                NOMBRE_CLINICA);

        sendSafe(cita.getUsuario().getCorreo(), subject, mensaje);
    }

    private void sendSafe(String to, String subject, String message) {
        try {
            emailService.sendEmail(to, subject, message);
        } catch (Exception e) {
            System.err.println("No se pudo enviar recordatorio a " + to + ": " + e.getMessage());
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