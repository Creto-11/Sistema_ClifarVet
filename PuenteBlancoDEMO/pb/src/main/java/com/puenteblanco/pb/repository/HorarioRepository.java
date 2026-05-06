package com.puenteblanco.pb.repository;

import com.puenteblanco.pb.entity.Horario;
import com.puenteblanco.pb.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByVeterinario(Veterinario veterinario);

    // Para filtrar horarios activos por día y veterinario
    List<Horario> findByVeterinarioIdAndDiaSemanaAndEstadoTrue(Long veterinarioId, String diaSemana);

    Optional<Horario> findByVeterinarioAndDiaSemana(Veterinario vet, String diaSemana);
}
