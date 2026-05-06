package com.puenteblanco.pb.controller.admin;

import com.puenteblanco.pb.dto.request.AdminUpdateAppointmentRequestDto;
import com.puenteblanco.pb.dto.response.AdminAppointmentResponseDto;
import com.puenteblanco.pb.services.interfaces.AdminAppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/appointments")
@RequiredArgsConstructor
public class AdminAppointmentController {

    private final AdminAppointmentService adminAppointmentService;

    // 1. Listar todas las citas
    @GetMapping
    public ResponseEntity<List<AdminAppointmentResponseDto>> listarCitas() {
        return ResponseEntity.ok(adminAppointmentService.listarCitas());
    }

    // 2. Editar cita: mascota, servicio y profesional a cargo
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarCita(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateAppointmentRequestDto dto
    ) {
        adminAppointmentService.actualizarCita(id, dto);
        return ResponseEntity.noContent().build();
    }
}
