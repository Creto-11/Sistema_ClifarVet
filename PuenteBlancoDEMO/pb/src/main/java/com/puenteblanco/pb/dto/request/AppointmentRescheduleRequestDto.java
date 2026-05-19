package com.puenteblanco.pb.dto.request;

import lombok.Data;

@Data
public class AppointmentRescheduleRequestDto {
    private String fecha;
    private String hora;
    private String motivoReprogramacion;
}