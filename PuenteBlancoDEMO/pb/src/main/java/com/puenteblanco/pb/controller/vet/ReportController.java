package com.puenteblanco.pb.controller.vet;

import com.puenteblanco.pb.dto.reportes.CitaPorFechaDTO;
import com.puenteblanco.pb.services.interfaces.ReportService;
import com.puenteblanco.pb.services.pdf.PdfReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vet/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final PdfReportService pdfReportService;

    @GetMapping("/citas-por-fecha")
    public List<CitaPorFechaDTO> getCitasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return reportService.obtenerCitasPorFecha(startDate, endDate);
    }

    @GetMapping("/citas-por-fecha/download")
public void descargarReporteCitasPorFecha(
        @RequestParam String startDate,
        @RequestParam String endDate,
        @RequestParam(required = false) String tipoServicio,
        @AuthenticationPrincipal UserDetails user,
        HttpServletResponse response) {

    try {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_citas_fecha.pdf");

        reportService.generarPdfCitasPorFecha(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate),
                response.getOutputStream(),
                user.getUsername(),
                tipoServicio
        );
    } catch (Exception e) {
        throw new RuntimeException("Error al descargar el reporte de citas por fecha", e);
    }
}

    @GetMapping("/citas-por-mascota/download")
public void downloadCitasPorMascotaPdf(
        @RequestParam String startDate,
        @RequestParam String endDate,
        @RequestParam(required = false) String tipoMascota,
        @RequestParam(required = false) String cliente,
        @AuthenticationPrincipal UserDetails userDetails,
        HttpServletResponse response
) throws IOException {
    String correo = userDetails.getUsername();
    String emitidoPor = reportService.obtenerNombreVeterinarioPorCorreo(correo);

    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=citas-por-mascota.pdf");

    reportService.generarPdfCitasPorMascota(
            start,
            end,
            response.getOutputStream(),
            tipoMascota,
            cliente,
            emitidoPor
    );
}


    @GetMapping("/citas-canceladas/download")
    public void downloadCitasCanceladasPdf(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletResponse response
    ) throws IOException {
        String correo = userDetails.getUsername();
        String emitidoPor = reportService.obtenerNombreVeterinarioPorCorreo(correo);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=citas-canceladas.pdf");

        reportService.exportCitasCanceladasReport(response.getOutputStream(), startDate, endDate, emitidoPor);
    }
}
