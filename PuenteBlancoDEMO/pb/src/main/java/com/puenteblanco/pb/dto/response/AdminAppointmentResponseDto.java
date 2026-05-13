package com.puenteblanco.pb.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminAppointmentResponseDto {

    private Long id;
    private String hora; // formato hh:mm a.m./p.m.
    private String fecha; // formato dd/MM/yyyy
    private String cliente;
    private String mascota;
    private String servicio;
    private String doctor; // veterinario o interno a cargo
    private String estadoCita; // confirmada, cancelada, completada, etc.
    private String estadoRegistro; // ACTIVO o INACTIVO
}
