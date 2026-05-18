package com.puenteblanco.pb.dto.request;

import lombok.Data;

@Data
public class AppointmentRequestDto {
    private Long servicioId;
    private Long veterinarioId;
    private String fecha; // formato: "yyyy-MM-dd"
    private String hora; // formato: "HH:mm"
    private Long petId;
    private String email;
}
