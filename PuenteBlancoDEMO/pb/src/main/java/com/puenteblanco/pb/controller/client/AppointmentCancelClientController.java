package com.puenteblanco.pb.controller.client;

import com.puenteblanco.pb.dto.request.AppointmentRescheduleRequestDto;
import com.puenteblanco.pb.dto.response.AppointmentCancelOptionDto;
import com.puenteblanco.pb.services.interfaces.AppointmentCancelClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/reschedule-appointments")
@RequiredArgsConstructor
public class AppointmentCancelClientController {

    private final AppointmentCancelClientService appointmentCancelClientService;

    @GetMapping
    public List<AppointmentCancelOptionDto> getReschedulableAppointments(Authentication authentication) {
        return appointmentCancelClientService.getReschedulableAppointments(authentication);
    }

    @PostMapping("/{id}/reschedule")
    public void rescheduleAppointment(@PathVariable Long id,
            @RequestBody AppointmentRescheduleRequestDto dto,
            Authentication authentication) {
        appointmentCancelClientService.rescheduleAppointment(id, dto, authentication);
    }
}