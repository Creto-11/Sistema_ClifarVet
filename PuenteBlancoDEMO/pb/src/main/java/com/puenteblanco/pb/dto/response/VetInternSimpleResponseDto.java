package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VetInternSimpleResponseDto {
    private Long id;
    private String nombres;
    private String apellidoPaterno;
}
