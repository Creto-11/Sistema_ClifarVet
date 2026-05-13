package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.exception.ResourceNotFoundException;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.services.interfaces.EmailService;
import com.puenteblanco.pb.services.interfaces.VetAssignInternService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class VetAssignInternServiceImpl implements VetAssignInternService {

    private final CitaRepository citaRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final String NOMBRE_CLINICA = "Clínica Veterinaria Puente Blanco";

    public VetAssignInternServiceImpl(CitaRepository citaRepository, UserRepository userRepository, EmailService emailService) {
        this.citaRepository = citaRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public void assignInternToAppointment(Long citaId, Long internId, String correoVeterinario) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        String correoAsignado = Optional.ofNullable(cita.getVeterinario())
                .map(v -> v.getUsuario())
                .map(User::getCorreo)
                .orElse(null);

        if (correoAsignado == null || !correoAsignado.equalsIgnoreCase(correoVeterinario)) {
            throw new ResourceNotFoundException("No puedes asignar una cita que no te pertenece");
        }

        User intern = userRepository.findById(internId)
                .orElseThrow(() -> new ResourceNotFoundException("Practicante no encontrado"));

        if (!intern.getRole().getId().equals(4L)) {
            throw new IllegalArgumentException("El usuario no es un practicante válido");
        }

        cita.setIntern(intern);
        cita.setEstado("DERIVADA");
        cita.setVistoInterno(false);
        citaRepository.save(cita);

        // Datos de la cita (adaptarlo según tu entidad)
        String fecha = cita.getFecha().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"));
        String hora = cita.getHora().toString(); // Ajusta el formato si es LocalTime
        String veterinarioNombre = cita.getVeterinario().getUsuario().getNombres();
        String mascotaNombre = cita.getPet().getName();
        String clienteNombre = cita.getUsuario().getNombres();

        // ➤ Email al cliente
        emailService.sendEmail(
                cita.getUsuario().getCorreo(),
                "Actualización de su cita",
                String.format(
                        "Estimado(a) %s,\n\nLe informamos que su cita programada para el día %s a las %s ha sido asignada a un practicante en formación, quien realizará la atención bajo la supervisión directa del veterinario responsable, %s.\n\nQueremos asegurarle que recibirá el mismo nivel de cuidado y profesionalismo durante toda la consulta.\n\nAtentamente,\n%s",
                        clienteNombre,
                        fecha,
                        hora,
                        veterinarioNombre,
                        NOMBRE_CLINICA
                )
        );

        // ➤ Email al practicante
        emailService.sendEmail(
                intern.getCorreo(),
                "Nueva cita asignada",
                String.format(
                        "Estimado(a) %s,\n\nSe le ha asignado una nueva cita programada para el día %s a las %s con el paciente %s (propietario: %s). Por favor, revise su panel para ver los detalles completos y prepárese adecuadamente.\n\nRecuerde que contará con la supervisión del veterinario responsable, %s, durante toda la atención.\n\nAtentamente,\n%s",
                        intern.getNombres(),
                        fecha,
                        hora,
                        mascotaNombre,
                        clienteNombre,
                        veterinarioNombre,
                        NOMBRE_CLINICA
                )
        );
    }
}
