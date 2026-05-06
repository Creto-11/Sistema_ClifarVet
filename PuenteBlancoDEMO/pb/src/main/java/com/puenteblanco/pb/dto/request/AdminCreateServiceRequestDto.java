package com.puenteblanco.pb.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateServiceRequestDto {

    @NotBlank
    private String descripcion;

    @NotNull
    @PositiveOrZero
    private Double precioBase;

    @NotNull
    private Long tipoServicioId;
}