package com.puenteblanco.pb.services.interfaces;

import java.time.LocalDate;

public interface VaccinePredictionService {

    LocalDate calcularProximaVacuna(Long petId);

}
