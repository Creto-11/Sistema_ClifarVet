package com.puenteblanco.pb.services.interfaces;
//tmb este modifique
import com.puenteblanco.pb.entity.Pet;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.repository.PetRepository;
import com.puenteblanco.pb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public Pet registerPet(Pet pet) {
        // Generar un petId incremental único
        String newPetId = generateNextPetId();
        pet.setPetId(newPetId);
        pet.setEstado(1); // Activar mascota al registrar
        return petRepository.save(pet);
    }

    private String generateNextPetId() {
        Optional<Pet> lastPet = petRepository.findTopByOrderByPetIdDesc();
        int nextNumber = 1;

        if (lastPet.isPresent()) {
            String lastId = lastPet.get().getPetId(); // Ej: PET-0023
            if (lastId != null && lastId.length() >= 8 && lastId.startsWith("PET-")) {
                try {
                    String numberPart = lastId.substring(4); // Extrae "0023"
                    nextNumber = Integer.parseInt(numberPart) + 1;
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Error al parsear el número de petId: " + lastId);
                    nextNumber = 1;
                }
            } else {
                System.out.println("⚠️ petId inválido detectado: " + lastId);
            }
        }

        return String.format("PET-%04d", nextNumber); // Formato: PET-0024
    }

    // ✅ Eliminación lógica
    public void deleteById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
        pet.setEstado(0); // Inactivar mascota
        petRepository.save(pet);
    }

    // ✅ Listar mascotas activas por email del dueño
    public List<Pet> getActivePetsByOwnerEmail(String email) {
        return petRepository.findByOwnerEmailAndEstado(email, 1);
    }

    // ✅ Obtener nombre completo del cliente por su correo
    public String getClientFullNameByEmail(String email) {
        return userRepository.findClientByCorreo(email)
                .map(user -> user.getNombres() + " " + user.getApellidoPaterno())
                .orElse("Usuario");
    }
}