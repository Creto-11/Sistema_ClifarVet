package com.puenteblanco.pb.repository;

import com.puenteblanco.pb.dto.response.AdminPaymentResponseDto;
import com.puenteblanco.pb.entity.Pago;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    boolean existsByCitaId(Long citaId);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.fechaPago BETWEEN :inicio AND :fin")
    double getTotalPagadoEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT MONTH(p.fechaPago) AS mes, COALESCE(SUM(p.monto), 0) AS total " +
       "FROM Pago p " +
       "WHERE YEAR(p.fechaPago) = :anio " +
       "GROUP BY MONTH(p.fechaPago) " +
       "ORDER BY mes")
    List<Object[]> getIngresosPorMes(@Param("anio") int anio);

    @Query("""
        SELECT new com.puenteblanco.pb.dto.response.AdminPaymentResponseDto(
            c.fecha,
            CONCAT(u.nombres, ' ', u.apellidoPaterno),
            s.descripcion,
            s.precioBase,
            'Tarjeta de crédito',
            CASE 
                WHEN c.estado = 'PAGADA' THEN 'COMPLETADO'
                ELSE 'PENDIENTE'
            END
        )
        FROM Cita c
        JOIN c.usuario u
        JOIN c.servicio s
        JOIN Pago p ON p.cita.id = c.id
        WHERE c.estado IN ('COMPLETADA', 'PAGADA')
    """)
    List<AdminPaymentResponseDto> findAllPaymentsForAdmin();
}
