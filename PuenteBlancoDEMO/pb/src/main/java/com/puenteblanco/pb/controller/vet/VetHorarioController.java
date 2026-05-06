package com.puenteblanco.pb.controller.vet;

import com.puenteblanco.pb.dto.request.HorarioUpdateRequest;
import com.puenteblanco.pb.services.interfaces.VetHorarioUpdateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vet/schedule")
public class VetHorarioController {

    private final VetHorarioUpdateService vetHorarioUpdateService;

    public VetHorarioController(VetHorarioUpdateService vetHorarioUpdateService) {
        this.vetHorarioUpdateService = vetHorarioUpdateService;
    }

    @PutMapping("/update")
    public void actualizarHorariosSemanaSiguiente(
            @RequestBody List<HorarioUpdateRequest> nuevosHorarios,
            Principal principal
    ) {
        String correoVeterinario = principal.getName();
        vetHorarioUpdateService.actualizarHorariosSemanaSiguiente(nuevosHorarios, correoVeterinario);
    }
}
