package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.response.VetClientWithPetResponseDto;
import com.puenteblanco.pb.dto.response.VetClinicalRecordResponseDto;
import com.puenteblanco.pb.dto.response.VetPatientListResponseDto;

import java.util.List;

public interface VetPatientHistoryService {
    List<VetPatientListResponseDto> getAllPatientsWithHistory();

    List<VetClinicalRecordResponseDto> getClinicalHistoryByPet(Long petId);

    List<VetClientWithPetResponseDto> obtenerClientesAtendidos(String correoVeterinario);

    String verificarDesparasitacion(Long petId);
}
