package com.puenteblanco.pb.controller.client;

import com.puenteblanco.pb.dto.response.AppointmentCancelOptionDto;
import com.puenteblanco.pb.services.interfaces.AppointmentCancelClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;  

@RestController
@RequestMapping("/api/client/cancel-appointments")
@RequiredArgsConstructor
public class AppointmentCancelClientController {

    private final AppointmentCancelClientService appointmentCancelClientService;

    @GetMapping
    public List<AppointmentCancelOptionDto> getCancelableAppointments(Authentication authentication) {
        return appointmentCancelClientService.getCancelableAppointments(authentication);
    }

    @PostMapping("/{id}/cancel")
    public void cancelAppointment(@PathVariable Long id,
                                  @RequestBody Map<String, String> body,
                                  Authentication authentication) {
        String motivo = body.get("motivoCancelacion");
        appointmentCancelClientService.cancelAppointment(id, motivo, authentication);
    }
}
