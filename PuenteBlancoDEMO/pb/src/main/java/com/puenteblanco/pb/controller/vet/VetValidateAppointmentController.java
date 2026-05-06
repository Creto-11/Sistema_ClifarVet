package com.puenteblanco.pb.controller.vet;

import com.puenteblanco.pb.dto.response.VetAppointmentInternDetailResponseDto;
import com.puenteblanco.pb.dto.response.VetAppointmentValidateResponseDto;
import com.puenteblanco.pb.services.interfaces.VetAppointmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vet/appointments/validate")
@RequiredArgsConstructor
public class VetValidateAppointmentController {

    private final VetAppointmentService vetAppointmentService;

    @GetMapping
    public List<VetAppointmentValidateResponseDto> getCitasEvaluadasPendientes() {
        return vetAppointmentService.getCitasEvaluadasPendientesValidacion();
    }

    @GetMapping("/{citaId}/detalle")
    public ResponseEntity<VetAppointmentInternDetailResponseDto> getDetalleEvaluacion(@PathVariable Long citaId) {
        VetAppointmentInternDetailResponseDto detalle = vetAppointmentService.getDetalleEvaluacion(citaId);
        return ResponseEntity.ok(detalle);
    }
}
