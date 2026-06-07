package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.Horario;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.HorarioRepository;
import com.puenteblanco.pb.services.interfaces.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;
    private final CitaRepository citaRepository;

    @Override
    public List<String> getAvailableTimeSlots(Long vetId, LocalDate fecha) {
        String diaSemana = capitalize(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es")));

        List<Horario> horarios = horarioRepository.findByVeterinarioIdAndDiaSemanaAndEstadoTrue(vetId, diaSemana);

        List<LocalTime> horasOcupadas = citaRepository
                .findByVeterinarioIdAndFechaAndEstadoIn(
                        vetId,
                        fecha,
                        Arrays.asList("PENDIENTE_PAGO", "PROGRAMADA", "PAGADA", "REPROGRAMADA"))
                .stream()
                .map(Cita::getHora)
                .toList();

        List<String> slots = new ArrayList<>();
        for (Horario horario : horarios) {
            LocalTime start = horario.getHoraComienzo();
            while (start.isBefore(horario.getHoraFin())) {
                if (!horasOcupadas.contains(start)) {
                    slots.add(start.toString());
                }
                start = start.plusMinutes(20);
            }
        }

        return slots;
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}