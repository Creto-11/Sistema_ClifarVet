package com.puenteblanco.pb.controller.vet;

import com.puenteblanco.pb.dto.response.VetClientWithPetResponseDto;
import com.puenteblanco.pb.dto.response.VetClinicalRecordResponseDto;
import com.puenteblanco.pb.dto.response.VetPatientListResponseDto;
import com.puenteblanco.pb.services.interfaces.VaccinePredictionService;
import com.puenteblanco.pb.services.interfaces.VetPatientHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vet/history")
@RequiredArgsConstructor
public class VetPatientHistoryController {

    private final VetPatientHistoryService vetPatientHistoryService;

    // Lista todos los pacientes que tienen al menos un historial médico
    @GetMapping
    @PreAuthorize("hasRole('VETERINARIAN')")
    public ResponseEntity<List<VetPatientListResponseDto>> getAllPatientsWithHistory() {
        List<VetPatientListResponseDto> patients = vetPatientHistoryService.getAllPatientsWithHistory();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{petId}/clinical")
    @PreAuthorize("hasRole('VETERINARIAN')")
    public ResponseEntity<List<VetClinicalRecordResponseDto>> getClinicalHistoryByPet(@PathVariable Long petId) {
        List<VetClinicalRecordResponseDto> history = vetPatientHistoryService.getClinicalHistoryByPet(petId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{petId}")
    public ResponseEntity<List<VetClinicalRecordResponseDto>> getClinicalHistory(@PathVariable Long petId) {
        List<VetClinicalRecordResponseDto> records = vetPatientHistoryService.getClinicalHistoryByPet(petId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/clientes-atendidos")
    @PreAuthorize("hasRole('VETERINARIAN')")
    public ResponseEntity<List<VetClientWithPetResponseDto>> listarClientesAtendidos(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<VetClientWithPetResponseDto> clientes = vetPatientHistoryService
                .obtenerClientesAtendidos(userDetails.getUsername());
        return ResponseEntity.ok(clientes);
    }

    private final VaccinePredictionService vaccinePredictionService;

    @GetMapping("/{petId}/next-vaccine")
    @PreAuthorize("hasRole('VETERINARIAN')")
    public ResponseEntity<LocalDate> getNextVaccine(@PathVariable Long petId) {

        LocalDate nextVaccine = vaccinePredictionService.calcularProximaVacuna(petId);

        return ResponseEntity.ok(nextVaccine);
    }

    @GetMapping("/{petId}/desparasitacion-alerta")
    @PreAuthorize("hasRole('VETERINARIAN')")
    public ResponseEntity<String> alertaDesparasitacion(@PathVariable Long petId) {

        String mensaje = vetPatientHistoryService.verificarDesparasitacion(petId);

        return ResponseEntity.ok(mensaje);
    }

}
