package com.puenteblanco.pb.controller.vet;

import com.puenteblanco.pb.dto.request.VetAssignInternRequestDto;
import com.puenteblanco.pb.services.interfaces.VetAssignInternService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/vet/appointments")
public class VetAssignInternController {

    private final VetAssignInternService vetAssignInternService;

    public VetAssignInternController(VetAssignInternService vetAssignInternService) {
        this.vetAssignInternService = vetAssignInternService;
    }

    
    /**
     * Asigna un practicante a una cita y notifica por correo.
     *
     * @param citaId   ID de la cita
     * @param request  Contiene el ID del practicante
     * @param principal Usuario autenticado (veterinario)
     * @return mensaje de éxito
     */

    @PutMapping("/{id}/assign-intern")
    public ResponseEntity<String> assignInternToAppointment(
            @PathVariable("id") Long citaId,
            @Valid @RequestBody VetAssignInternRequestDto request,
            Principal principal
    ) {
        vetAssignInternService.assignInternToAppointment(
            citaId, 
            request.getInternId(), 
            principal.getName()
            );
        return ResponseEntity.ok("Cita derivada correctamente. Correos enviados al cliente y practicante.");
    }
}
