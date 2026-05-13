package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VetHistoryPetResponseDto {
    private String nombre;
    private String tipo;
    private String raza;
}