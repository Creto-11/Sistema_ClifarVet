package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.dashboard.DashboardVetName;
import com.puenteblanco.pb.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class vetDashboardService { // Cambiado a mayúscula inicial
    private final VeterinarioRepository veterinarioRepository; // Usa tu repositorio actual

    public DashboardVetName getVetName(Authentication authentication) {
        String email = authentication.getName();
        return veterinarioRepository.findByUsuarioCorreo(email)
            .map(vet -> new DashboardVetName(vet.getNombreCompleto())) // Asegúrate que el campo se llama así
            .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
    }
}