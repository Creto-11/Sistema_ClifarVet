package com.puenteblanco.pb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VeterinarianListResponseDto {

    private Long id;
    private String nombreCompleto;
    private String especialidad;
    private String genero;
    private String descripcion;
    private List<String> diasDisponibles; // Ej: ["Monday", "Wednesday", "Friday"]
    private String horario; // Ej: "9:00 AM - 5:00 PM"
}
