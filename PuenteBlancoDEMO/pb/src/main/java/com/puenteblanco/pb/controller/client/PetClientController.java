// File: PetClientController.java
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
@CrossOrigin(origins = "*") //MODIFICACIÓN POR CRETO
public class PetClientController {

    private final PetRepository petRepository;

    @GetMapping
    public ResponseEntity<List<Pet>> getPets(Authentication auth) {
        String email = auth.getName(); // Extraído del token JWT
        System.out.println("Buscando mascotas para: " + email);

        List<Pet> pets = petRepository.findByOwnerEmail(email);
        return ResponseEntity.ok(pets);
    }
    //MODIFICACION POR CRETO: Conexion con Flutter
    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet, Authentication auth) {
        // Extraemos el email del token
        String email = auth.getName(); 
        
        // Forzamos que la mascota sea del dueño del token. 
        pet.setOwnerEmail(email); 
        
        // Guardamos usando tu servicio existente
        // (Asegúrate de llamar a tu petService o petRepository aquí)
        Pet savedPet = petRepository.save(pet);
        return ResponseEntity.ok(savedPet);
    }
}
