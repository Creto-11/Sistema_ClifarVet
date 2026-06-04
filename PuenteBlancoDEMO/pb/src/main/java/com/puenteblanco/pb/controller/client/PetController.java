package com.puenteblanco.pb.controller.client;

import com.puenteblanco.pb.entity.Pet;
import com.puenteblanco.pb.security.AuthUtils;
import com.puenteblanco.pb.services.interfaces.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public String showPetForm(Model model) {
        model.addAttribute("pet", new Pet());

        String email = AuthUtils.getAuthenticatedEmail();

        if (email != null) {
            String fullName = petService.getClientFullNameByEmail(email);
            model.addAttribute("dashboard", new Object() {
                public String getFullName() {
                    return fullName;
                }
            });
        }

        return "add-pet";
    }

    @PostMapping
    public String registerPet(
            @ModelAttribute Pet pet,
            @RequestParam(value = "birthDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate) {

        String email = AuthUtils.getAuthenticatedEmail();

        if (email == null) {
            throw new RuntimeException("No authenticated user found");
        }

        if (birthDate != null) {
            if (birthDate.isAfter(LocalDate.now())) {
                throw new RuntimeException("La fecha de nacimiento de la mascota no puede ser futura.");
            }
            pet.setAge(Period.between(birthDate, LocalDate.now()).getYears());
        }

        if (pet.getAge() == null || pet.getAge() < 0) {
            throw new RuntimeException("Debe registrar una edad válida para la mascota.");
        }

        pet.setOwnerEmail(email);
        petService.registerPet(pet);

        return "redirect:/dashboard?success=true";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePetById(@PathVariable Long id) {
        try {
            petService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar la mascota.");
        }
    }

    @GetMapping("/actives")
    @ResponseBody
    public ResponseEntity<?> getActivePetsForCurrentUser() {
        String email = AuthUtils.getAuthenticatedEmail();
        if (email == null) {
            return ResponseEntity.badRequest().body("Usuario no autenticado");
        }

        List<Pet> activePets = petService.getActivePetsByOwnerEmail(email);
        return ResponseEntity.ok(activePets);
    }
}