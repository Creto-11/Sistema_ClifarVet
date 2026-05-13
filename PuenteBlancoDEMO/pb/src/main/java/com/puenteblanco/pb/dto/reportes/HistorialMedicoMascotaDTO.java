package com.puenteblanco.pb.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialMedicoMascotaDTO {

    private LocalDate fecha;
    private String servicio;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    
    // Campos informativos de la mascota (si se desea mostrar en cabecera del PDF)
    private String nombreMascota;
    private String tipoMascota;
    private String raza;
    private String propietario;
}
