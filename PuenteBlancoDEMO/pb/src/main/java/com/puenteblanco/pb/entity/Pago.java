package com.puenteblanco.pb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long monto; // En centavos: 5000 = S/ 50.00

    private String moneda; // "pen"

    private String descripcion;

    private String estado;

    private String stripePaymentIntentId;

    private LocalDateTime fechaPago;

    @OneToOne
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private Cita cita;
}
