package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.request.AppointmentRescheduleRequestDto;
import com.puenteblanco.pb.dto.response.AppointmentCancelOptionDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AppointmentCancelClientService {

    List<AppointmentCancelOptionDto> getReschedulableAppointments(Authentication auth);

    void rescheduleAppointment(Long id, AppointmentRescheduleRequestDto dto, Authentication authentication);
}
