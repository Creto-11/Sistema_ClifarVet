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

            String servicio = atencion.getCita().getServicio().getDescripcion().toLowerCase();

            if (servicio.contains("vacuna")) {

                LocalDate fechaVacuna = atencion.getCita().getFecha();

                return fechaVacuna.plusYears(1);
            }
        }

        return null;
    }
}