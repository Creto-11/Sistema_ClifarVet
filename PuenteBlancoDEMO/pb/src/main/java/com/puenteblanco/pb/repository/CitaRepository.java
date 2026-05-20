package com.puenteblanco.pb.repository;

import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByUsuario(User usuario);

    List<Cita> findByUsuarioCorreoAndEstadoIgnoreCase(String correo, String estado);

    List<Cita> findAllByUsuarioCorreoAndEstado(String correo, String estado);

    @Query("SELECT DISTINCT c.usuario FROM Cita c WHERE c.veterinario.usuario.id = :vetId AND c.estado = 'COMPLETADA'")
    List<User> findClientesUnicosPorVeterinario(@Param("vetId") Long vetId);

    List<Cita> findByVeterinarioIdAndFecha(Long veterinarioId, LocalDate fecha);

    List<Cita> findByVeterinarioIdAndFechaBetween(Long veterinarioId, LocalDate desde, LocalDate hasta);

    List<Cita> findByVeterinarioIdAndEstado(Long vetId, String estado);

    List<Cita> findByVeterinarioIdAndFechaBetweenAndEstado(Long vetId, LocalDate desde, LocalDate hasta,
            String estado);

    List<Cita> findByVeterinarioIdAndFechaAndEstado(Long veterinarioId, LocalDate fecha, String estado);

    List<Cita> findByVeterinarioId(Long veterinarioId);

    List<Cita> findByFechaBetween(LocalDate startDate, LocalDate endDate); // REPORTES

    List<Cita> findByIntern_IdAndEstado(Long internId, String estado); // Para interno

    @Query("SELECT c FROM Cita c WHERE c.intern.id = :internId AND (c.estado = 'COMPLETADA' OR c.estado = 'PAGADA')")
    List<Cita> findCitasValidadasPorIntern(@Param("internId") Long internId); // Para Interno

    List<Cita> findByEstado(String estado); // PARA EVALUADAS

    int countByVeterinarioIdAndFecha(Long vetId, LocalDate fecha);

    int countByVeterinarioIdAndFechaBetweenAndEstado(Long vetId, LocalDate desde, LocalDate hasta, String estado);

    int countByFecha(LocalDate fecha); // Admin

    int countByEstadoAndFechaBetween(String estado, LocalDate inicio, LocalDate fin); // Admin

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = 'COMPLETADA' AND c.id NOT IN (SELECT a.cita.id FROM AtencionMedica a)")
    int countCompletadasSinAtencion();

    int countByEstado(String estado);

    // Método para encontrar citas que deben recibir un recordatorio 10 minutos
    // después de la reserva
    @Query("SELECT c FROM Cita c WHERE c.fecha = :today AND c.hora BETWEEN :now AND :nowPlus10Minutes")
    List<Cita> findCitasForReminder(@Param("today") LocalDate today,
            @Param("now") LocalTime now,
            @Param("nowPlus10Minutes") LocalTime nowPlus10Minutes);

    // Método para encontrar citas que deben recibir un recordatorio 30 minutos
    // antes
    @Query("SELECT c FROM Cita c WHERE c.fecha = :today AND c.hora BETWEEN :reminderTime AND :reminderTimePlus30Minutes")
    List<Cita> findCitasForReminder30MinutesBefore(@Param("today") LocalDate today,
            @Param("reminderTime") LocalTime reminderTime,
            @Param("reminderTimePlus30Minutes") LocalTime reminderTimePlus30Minutes);

    // ✔ Buscar citas DERIVADA por intern y que no hayan sido vistas (false o NULL)
    @Query("SELECT c FROM Cita c WHERE c.intern = :intern AND c.estado = :estado AND (c.vistoInterno = false OR c.vistoInterno IS NULL)")
    List<Cita> findDerivadasNoVistas(@Param("intern") User intern, @Param("estado") String estado);

    // Método de reprogramación de citas que evita que sea en un horario ocupado
    @Query("""
            SELECT COUNT(c)
            FROM Cita c
            WHERE c.veterinario.id = :veterinarioId
            AND c.fecha = :fecha
            AND c.hora = :hora
            AND UPPER(c.estado) IN ('PROGRAMADA', 'PAGADA')
            AND c.id <> :citaId
            """)
    long countActiveAppointmentsAtSameSlot(
            @Param("veterinarioId") Long veterinarioId,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora,
            @Param("citaId") Long citaId);

    int countByCantidadReprogramacionesGreaterThanAndFechaBetween(
            Integer cantidad,
            LocalDate inicio,
            LocalDate fin);

}
