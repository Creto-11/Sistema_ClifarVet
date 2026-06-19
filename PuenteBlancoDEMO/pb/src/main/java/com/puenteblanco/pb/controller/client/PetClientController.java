package com.puenteblanco.pb.controller.client;

import com.puenteblanco.pb.entity.Pet;
import com.puenteblanco.pb.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/pets")
@RequiredArgsConstructor
public class PetClientController {

    private final PetRepository petRepository;

    @GetMapping
    public ResponseEntity<?> getPets(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.badRequest().body("Usuario no autenticado");
        }

        String email = auth.getName();
        List<Pet> pets = petRepository.findByOwnerEmailAndEstado(email, 1);
        return ResponseEntity.ok(pets);
    }
}