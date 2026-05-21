package com.puenteblanco.pb.controller.client;

import com.puenteblanco.pb.dto.request.AppointmentRequestDto;
import com.puenteblanco.pb.dto.response.AppointmentBookingResponseDto;
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
    public ResponseEntity<AppointmentBookingResponseDto> bookAppointment(
            @RequestBody AppointmentRequestDto dto,
            Authentication authentication) {
        AppointmentBookingResponseDto response = appointmentClientService.bookAppointment(authentication, dto);
        return ResponseEntity.ok(response);
    }
}
