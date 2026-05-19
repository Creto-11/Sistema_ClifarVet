package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.AppointmentRequestDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.Servicio;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.entity.Veterinario;
import com.puenteblanco.pb.entity.Pet;
import com.puenteblanco.pb.repository.PetRepository;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.ServiceRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.repository.VeterinarioRepository;
import com.puenteblanco.pb.services.interfaces.AppointmentClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentClientServiceImpl implements AppointmentClientService {

        private final CitaRepository citaRepository;
        private final ServiceRepository servicioRepository;
        private final VeterinarioRepository veterinarioRepository;
        private final UserRepository userRepository;
        private final PetRepository petRepository;

        @Autowired
        private JavaMailSender mailSender;

        @Override
        @Transactional
        public void bookAppointment(Authentication auth, AppointmentRequestDto dto) {
                String correo = auth.getName();
                User user = userRepository.findByCorreo(correo)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Servicio servicio = servicioRepository.findById(dto.getServicioId())
                                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

                Veterinario veterinario = veterinarioRepository.findById(dto.getVeterinarioId())
                                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

                Pet pet = petRepository.findById(dto.getPetId())
                                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

                // Guardar la cita en la base de datos
                Cita cita = Cita.builder()
                                .usuario(user)
                                .servicio(servicio)
                                .veterinario(veterinario)
                                .pet(pet)
                                .fecha(LocalDate.parse(dto.getFecha()))
                                .hora(LocalTime.parse(dto.getHora()))
                                .precioCobrado(servicio.getPrecioBase())
                                .estado("PROGRAMADA")
                                .build();

                citaRepository.save(cita);

                // Enviar el correo de confirmación inmediato
                sendAppointmentReminderEmail(dto, user.getCorreo(), veterinario.getNombreCompleto());
        }

        // Enviar correo de confirmación inmediato
        private void sendAppointmentReminderEmail(AppointmentRequestDto dto, String userEmail,
                        String veterinarianName) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(userEmail);
                message.setSubject("Recordatorio de Cita - Puente Blanco");
                message.setText("Estimado cliente,\n\n" +
                                "Le recordamos que la cita para su mascota está agendada para el " + dto.getFecha() +
                                " a las " + dto.getHora() + " con el veterinario " + veterinarianName +
                                ".\n\nGracias por elegirnos.\n\nAtentamente,\nClínica y Veterinaria Puente Blanco");

                mailSender.send(message);
        }

        // Enviar correo 30 minutos antes de la cita
        @Scheduled(cron = "0 0/30 * * * ?") // Ejecuta cada 30 minutos
        public void sendReminder30MinutesBefore() {
                // Buscar todas las citas que necesitan un recordatorio 30 minutos antes
                List<Cita> citas = citaRepository.findCitasForReminder30MinutesBefore(LocalDate.now(), LocalTime.now(),
                                LocalTime.now().plusMinutes(30));

                // Enviar recordatorio
                for (Cita cita : citas) {
                        sendAppointmentReminderEmail(new AppointmentRequestDto(), cita.getUsuario().getCorreo(),
                                        cita.getVeterinario().getNombreCompleto());
                }
        }

        // Tarea programada para enviar recordatorio 10 minutos después de la reserva
        @Scheduled(fixedRate = 600000) // Ejecuta cada 10 minutos (600,000 ms)
        public void scheduleReminderEmail() {
                // Verificar si han pasado 10 minutos desde que se registró la cita
                List<Cita> citas = citaRepository.findCitasForReminder(LocalDate.now(), LocalTime.now(),
                                LocalTime.now().plusMinutes(10));
                for (Cita cita : citas) {
                        sendAppointmentReminderEmail(new AppointmentRequestDto(), cita.getUsuario().getCorreo(),
                                        cita.getVeterinario().getNombreCompleto());
                }
        }
}
