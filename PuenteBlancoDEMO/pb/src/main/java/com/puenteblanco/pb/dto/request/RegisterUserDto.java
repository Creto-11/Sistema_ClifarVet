package com.puenteblanco.pb.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidoPaterno;

    @NotBlank
    private String apellidoMaterno;

    @NotBlank
    private String contrasena;

    @NotBlank
    private String numeroIdentidad;

    @Pattern(regexp = "^(Masculino|Femenino|No Binario)$", message = "El sexo debe ser Masculino, Femenino o No Binario")
    @NotBlank
    private String sexo;

    @NotBlank
    private String telefono;

    @NotNull
    private LocalDate fechaNacimiento;

    @Email
    @NotBlank
    private String correo;

    @NotBlank
    private String direccion;

    @NotNull
    private Long tipoDocumentoId;
}
