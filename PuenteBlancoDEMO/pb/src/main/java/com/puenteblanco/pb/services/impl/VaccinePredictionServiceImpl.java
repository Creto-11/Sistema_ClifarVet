package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.entity.AtencionMedica;
import com.puenteblanco.pb.repository.AtencionMedicaRepository;
import com.puenteblanco.pb.services.interfaces.VaccinePredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccinePredictionServiceImpl implements VaccinePredictionService {

    private final AtencionMedicaRepository atencionMedicaRepository;

    @Override
    public LocalDate calcularProximaVacuna(Long petId) {

        List<AtencionMedica> historial = atencionMedicaRepository.findByCitaPetIdOrderByCitaFechaAsc(petId);

        for (int i = historial.size() - 1; i >= 0; i--) {

            AtencionMedica atencion = historial.get(i);

            if (!atencion.isActivo() || atencion.getCita() == null || atencion.getCita().getServicio() == null) {
                continue;
            }

            String estadoCita = atencion.getCita().getEstado() != null ? atencion.getCita().getEstado() : "";
            String estadoValidacion = atencion.getEstadoValidacion() != null ? atencion.getEstadoValidacion() : "";

            boolean atencionConfirmada = "COMPLETADA".equalsIgnoreCase(estadoCita)
                    || "COMPLETADA".equalsIgnoreCase(estadoValidacion);

            if (!atencionConfirmada) {
                continue;
            }

            String servicio = atencion.getCita().getServicio().getDescripcion();
            servicio = servicio != null ? servicio.toLowerCase() : "";

            if (servicio.contains("vacuna") || servicio.contains("vacunación") || servicio.contains("vacunacion")) {

                LocalDate fechaVacuna = atencion.getCita().getFecha();

                return fechaVacuna.plusYears(1);
            }
        }

        return null;
    }
}