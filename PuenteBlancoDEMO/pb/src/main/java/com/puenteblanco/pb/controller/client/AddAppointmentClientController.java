package com.puenteblanco.pb.controller.client;

import com.puenteblanco.pb.dto.request.AppointmentRequestDto;
import com.puenteblanco.pb.services.interfaces.AppointmentClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/appointments")
@RequiredArgsConstructor
public class AddAppointmentClientController {

    private final AppointmentClientService appointmentClientService;

    @PostMapping
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentRequestDto dto, Authentication authentication) {
        // Llamar al servicio para reservar la cita y enviar el correo
        appointmentClientService.bookAppointment(authentication, dto);

        // Confirmar que la cita fue registrada y el correo enviado
        return ResponseEntity.ok("Cita registrada exitosamente. Un correo de recordatorio ha sido enviado.");
    }
}
