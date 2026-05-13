package com.puenteblanco.pb.controller.interno;

import com.puenteblanco.pb.dto.request.InternAttendRequestDto;
import com.puenteblanco.pb.services.interfaces.InternAttendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intern/appointments")
@RequiredArgsConstructor
public class InternAttendController {

    private final InternAttendService internAttendService;
    

    @PostMapping("/{id}/attend")
public ResponseEntity<Void> registrarAtencion(
        @PathVariable("id") Long citaId,
        @RequestBody InternAttendRequestDto dto,
        Principal principal
) {
    String emailInterno = principal.getName(); // Este valor viene del JWT
    internAttendService.registrarAtencionMedica(citaId, dto, emailInterno);
    return ResponseEntity.ok().build();
}
}
