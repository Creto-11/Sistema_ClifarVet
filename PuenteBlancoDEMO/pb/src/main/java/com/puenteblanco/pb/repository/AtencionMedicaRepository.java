package com.puenteblanco.pb.repository;

import com.puenteblanco.pb.entity.AtencionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AtencionMedicaRepository extends JpaRepository<AtencionMedica, Long> {

    Optional<AtencionMedica> findByCitaIdAndActivoTrue(Long citaId);

    @Query("""
            SELECT a FROM AtencionMedica a
            JOIN FETCH a.cita c
            JOIN FETCH c.pet p
            JOIN FETCH c.usuario u
            WHERE a.activo = true
            ORDER BY c.fecha DESC, c.hora DESC
            """)
    List<AtencionMedica> findAllWithPetAndUser();

    @Query("""
            SELECT a FROM AtencionMedica a
            JOIN FETCH a.cita c
            JOIN FETCH c.pet p
            JOIN FETCH c.usuario u
            LEFT JOIN FETCH c.servicio s
            WHERE p.id = :petId AND a.activo = true
            ORDER BY c.fecha DESC, c.hora DESC
            """)
    List<AtencionMedica> findByPetId(@Param("petId") Long petId);

    Optional<AtencionMedica> findByCita_Id(Long citaId);

    @Query("""
            SELECT a FROM AtencionMedica a
            JOIN FETCH a.cita c
            JOIN FETCH c.pet p
            JOIN FETCH c.servicio s
            WHERE p.id = :petId AND a.activo = true
            ORDER BY c.fecha ASC, c.hora ASC
            """)
    List<AtencionMedica> findByCitaPetIdOrderByCitaFechaAsc(@Param("petId") Long petId);

}
