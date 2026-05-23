package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.request.AppointmentRequestDto;
import com.puenteblanco.pb.dto.response.AppointmentBookingResponseDto;
import org.springframework.security.core.Authentication;

public interface AppointmentClientService {
    AppointmentBookingResponseDto bookAppointment(Authentication auth, AppointmentRequestDto dto);
}