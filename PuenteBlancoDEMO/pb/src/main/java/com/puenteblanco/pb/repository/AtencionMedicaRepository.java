package com.puenteblanco.pb.repository;

import com.puenteblanco.pb.entity.AtencionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface AtencionMedicaRepository extends JpaRepository<AtencionMedica, Long> {

    Optional<AtencionMedica> findByCitaIdAndActivoTrue(Long citaId);

    @Query("SELECT a FROM AtencionMedica a JOIN FETCH a.cita c JOIN FETCH c.pet p JOIN FETCH c.usuario u")
    List<AtencionMedica> findAllWithPetAndUser();

    @Query("SELECT a FROM AtencionMedica a JOIN FETCH a.cita c JOIN FETCH c.pet p JOIN FETCH c.usuario u WHERE p.id = :petId")
    List<AtencionMedica> findByPetId(@Param("petId") Long petId);

    Optional<AtencionMedica> findByCita_Id(Long citaId);

    List<AtencionMedica> findByCitaPetIdOrderByCitaFechaAsc(Long petId);

}
