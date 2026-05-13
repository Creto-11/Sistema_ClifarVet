package com.puenteblanco.pb.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminCreateUserRequestDto {

    @NotNull
    private Long tipoDocumentoId;

    @NotBlank
    private String numeroIdentidad;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidoPaterno;

    @NotBlank
    private String apellidoMaterno;

    @NotBlank
    private String telefono;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotBlank
    private String sexo;

    @NotBlank
    private String direccion;

    @Email
    @NotBlank
    private String correo;

    @NotNull
    private Long roleId;

    @NotBlank
    private String contrasena;
}
