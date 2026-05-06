package com.puenteblanco.pb.controller.vet;

import com.puenteblanco.pb.services.pdf.PdfMedicalHistoryService;
import com.puenteblanco.pb.services.interfaces.VetMedicalHistoryReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/vet/reports/history")
@RequiredArgsConstructor
public class VetMedicalHistoryReportController {

    private final VetMedicalHistoryReportService historyReportService;
    private final PdfMedicalHistoryService pdfMedicalHistoryService;

    @GetMapping("/{petId}/download")
    public ResponseEntity<Resource> descargarHistorialPdf(
            @PathVariable Long petId,
            @AuthenticationPrincipal String veterinarioNombre
    ) {
        var historial = historyReportService.obtenerHistorialMedico(petId);
        System.out.println("🐾 Entrando al endpoint de descarga para petId=" + petId + " por " + veterinarioNombre);

        if (historial.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        String nombreArchivo = "historial_mascota_" + petId + ".pdf";
        String rutaPdf = pdfMedicalHistoryService.generarPdfHistorialMascota(nombreArchivo, historial);

        if (rutaPdf == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        File archivo = new File(rutaPdf);
        if (!archivo.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(archivo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(nombreArchivo).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}