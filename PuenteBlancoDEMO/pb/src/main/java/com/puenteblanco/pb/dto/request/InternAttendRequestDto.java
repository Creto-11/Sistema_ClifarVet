package com.puenteblanco.pb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternAttendRequestDto {
    private String observacionesClinicas;
    private String diagnostico;
    private String tratamiento;
    private String prescripcion;
}
