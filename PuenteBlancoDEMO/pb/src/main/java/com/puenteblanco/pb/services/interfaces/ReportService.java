package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.reportes.CitaCanceladaDTO;
import com.puenteblanco.pb.dto.reportes.CitaPorFechaDTO;
import com.puenteblanco.pb.dto.reportes.CitaPorMascotaDTO;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    // Reporte general de citas por fecha
    List<CitaPorFechaDTO> obtenerCitasPorFecha(LocalDate startDate, LocalDate endDate);
    List<CitaPorFechaDTO> obtenerCitasPorFechaFiltrado(LocalDate startDate, LocalDate endDate, String tipoServicio);
    void generarPdfCitasPorFecha(LocalDate startDate, LocalDate endDate, OutputStream outputStream, String emitidoPor, String tipoServicio);

    // Reporte por mascota
    List<CitaPorMascotaDTO> obtenerCitasPorMascotaFiltrado(LocalDate startDate, LocalDate endDate, String tipoMascota, String cliente);
    void generarPdfCitasPorMascota(LocalDate startDate, LocalDate endDate, OutputStream outputStream, String tipoMascota, String cliente, String emitidoPor);

    // Reporte de citas canceladas
    void exportCitasCanceladasReport(OutputStream outputStream, String startDate, String endDate, String emitidoPor);

    // Utilitario
    String obtenerNombreVeterinarioPorCorreo(String correo);
}
