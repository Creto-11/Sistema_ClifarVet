package com.puenteblanco.pb.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminServiceResponseDto {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String categoria;
    private boolean estado;
}
