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

        List<Cita> findByVeterinarioIdAndFechaAndEstadoIn(
                        Long veterinarioId,
                        LocalDate fecha,
                        List<String> estados);

        List<Cita> findByFechaBetween(LocalDate startDate, LocalDate endDate);

        List<Cita> findByIntern_IdAndEstado(Long internId, String estado);

        @Query("SELECT c FROM Cita c WHERE c.intern.id = :internId AND (c.estado = 'COMPLETADA' OR c.estado = 'PAGADA')")
        List<Cita> findCitasValidadasPorIntern(@Param("internId") Long internId);

        List<Cita> findByEstado(String estado);

        int countByVeterinarioIdAndFecha(Long vetId, LocalDate fecha);

        int countByVeterinarioIdAndFechaBetweenAndEstado(Long vetId, LocalDate desde, LocalDate hasta, String estado);

        int countByFecha(LocalDate fecha);

        int countByEstadoAndFechaBetween(String estado, LocalDate inicio, LocalDate fin);

        @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = 'COMPLETADA' AND c.id NOT IN (SELECT a.cita.id FROM AtencionMedica a)")
        int countCompletadasSinAtencion();

        int countByEstado(String estado);

        @Query("SELECT c FROM Cita c WHERE c.fecha = :today AND c.hora BETWEEN :now AND :nowPlus10Minutes")
        List<Cita> findCitasForReminder(@Param("today") LocalDate today,
                        @Param("now") LocalTime now,
                        @Param("nowPlus10Minutes") LocalTime nowPlus10Minutes);

        @Query("SELECT c FROM Cita c WHERE c.fecha = :today AND c.hora BETWEEN :reminderTime AND :reminderTimePlus30Minutes")
        List<Cita> findCitasForReminder30MinutesBefore(@Param("today") LocalDate today,
                        @Param("reminderTime") LocalTime reminderTime,
                        @Param("reminderTimePlus30Minutes") LocalTime reminderTimePlus30Minutes);

        @Query("SELECT c FROM Cita c WHERE c.intern = :intern AND c.estado = :estado AND (c.vistoInterno = false OR c.vistoInterno IS NULL)")
        List<Cita> findDerivadasNoVistas(@Param("intern") User intern, @Param("estado") String estado);

        @Query("""
                        SELECT COUNT(c)
                        FROM Cita c
                        WHERE c.veterinario.id = :veterinarioId
                        AND c.fecha = :fecha
                        AND c.hora = :hora
                        AND UPPER(c.estado) IN ('PENDIENTE_PAGO', 'PROGRAMADA', 'PAGADA', 'REPROGRAMADA')
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

        @Query("""
                        SELECT c
                        FROM Cita c
                        WHERE c.fecha = :fecha
                        AND c.hora = :hora
                        AND UPPER(c.estado) IN ('PROGRAMADA', 'REPROGRAMADA')
                        """)
        List<Cita> findCitasConfirmadasForReminderAt(
                        @Param("fecha") LocalDate fecha,
                        @Param("hora") LocalTime hora);

}