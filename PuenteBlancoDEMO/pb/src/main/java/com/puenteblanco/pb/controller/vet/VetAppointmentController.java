package com.puenteblanco.pb.controller.vet;

import com.puenteblanco.pb.dto.response.VetAppointmentResponseDto;
import com.puenteblanco.pb.dto.request.VetAppointmentStatusUpdateRequestDto;
import com.puenteblanco.pb.services.interfaces.VetAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vet")
@RequiredArgsConstructor
public class VetAppointmentController {

    private final VetAppointmentService vetAppointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<List<VetAppointmentResponseDto>> getAppointmentsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<VetAppointmentResponseDto> citas = vetAppointmentService.getAppointmentsForDate(date);
        return ResponseEntity.ok(citas);
    }

    @PutMapping("/appointments/{citaId}/estado")
    public ResponseEntity<Void> actualizarEstadoCita(
        @PathVariable Long citaId,
        @RequestBody VetAppointmentStatusUpdateRequestDto dto) {
            vetAppointmentService.actualizarEstadoCita(citaId, dto.getEstado());
            return ResponseEntity.ok().build();
        }
}
