package com.puenteblanco.pb.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String token;
    private String nombreCompleto;
    private String rol;
}