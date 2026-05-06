package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.reportes.HistorialMedicoMascotaDTO;

import java.util.List;

public interface VetMedicalHistoryReportService {
    List<HistorialMedicoMascotaDTO> obtenerHistorialMedico(Long petId);
}
