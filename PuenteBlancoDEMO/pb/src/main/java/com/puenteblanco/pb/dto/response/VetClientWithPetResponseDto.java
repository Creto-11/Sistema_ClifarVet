package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VetClientWithPetResponseDto {
    private String nombreCompleto;
    private String correo;
    private List<VetHistoryPetResponseDto> mascotas;
}
