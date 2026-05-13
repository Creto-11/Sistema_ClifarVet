package com.puenteblanco.pb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "atenciones_medicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtencionMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private Cita cita;

    @Column(name = "observaciones_clinicas", columnDefinition = "TEXT")
    private String observacionesClinicas;

    @Column(name = "diagnostico", length = 255)
    private String diagnostico;

    @Column(name = "tratamiento", length = 255)
    private String tratamiento;

    @Column(name = "prescripcion", columnDefinition = "TEXT")
    private String prescripcion;

    // Nuevo: practicante que realiza la atención
    @Column(name = "registrada_por", nullable = false)
    private Long registradaPorId;

    /**
     * Estado de validación:
     * - PENDIENTE: aún no ha sido revisada
     * - RECHAZADA: devuelta al practicante para corrección
     * - COMPLETADA: validada y aceptada por el veterinario
     */
    @Column(name = "estado_validacion", nullable = false)
    private String estadoValidacion;

    @Column(nullable = false)
    private boolean activo = true;
}