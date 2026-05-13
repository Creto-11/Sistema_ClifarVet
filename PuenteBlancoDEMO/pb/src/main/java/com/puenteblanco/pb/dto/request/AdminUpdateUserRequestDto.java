package com.puenteblanco.pb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateUserRequestDto {

    @NotBlank
    private String nombre;

    @NotNull
    private Long roleId;

}
