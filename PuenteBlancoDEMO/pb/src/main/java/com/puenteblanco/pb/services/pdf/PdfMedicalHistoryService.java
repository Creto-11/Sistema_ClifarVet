package com.puenteblanco.pb.services.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.puenteblanco.pb.dto.reportes.HistorialMedicoMascotaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfMedicalHistoryService {

    public String generarPdfHistorialMascota(String nombreArchivo, List<HistorialMedicoMascotaDTO> historial) {
        String rutaArchivo = "storage/reportes/" + nombreArchivo;
        Document documento = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();

            // Título
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
            Paragraph titulo = new Paragraph("Historial Médico de Mascota", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            documento.add(titulo);

            if (!historial.isEmpty()) {
                HistorialMedicoMascotaDTO info = historial.get(0);
                Font fontInfo = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.DARK_GRAY);

                Paragraph datosMascota = new Paragraph(String.format(
                        "Nombre: %s\nTipo: %s\nRaza: %s\nPropietario: %s",
                        info.getNombreMascota(), info.getTipoMascota(),
                        info.getRaza(), info.getPropietario()
                ), fontInfo);
                datosMascota.setSpacingAfter(20);
                documento.add(datosMascota);
            }

            // Tabla de historial
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10);
            tabla.setWidths(new float[]{2, 3, 4, 4, 4});

            Font fontCabecera = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor colorCabecera = new BaseColor(44, 62, 80);

            String[] encabezados = {"Fecha", "Servicio", "Diagnóstico", "Tratamiento", "Observaciones"};
            for (String encabezado : encabezados) {
                PdfPCell celda = new PdfPCell(new Phrase(encabezado, fontCabecera));
                celda.setBackgroundColor(colorCabecera);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(5);
                tabla.addCell(celda);
            }

            Font fontFila = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (HistorialMedicoMascotaDTO item : historial) {
                tabla.addCell(new PdfPCell(new Phrase(item.getFecha().format(formatter), fontFila)));
                tabla.addCell(new PdfPCell(new Phrase(item.getServicio(), fontFila)));
                tabla.addCell(new PdfPCell(new Phrase(item.getDiagnostico(), fontFila)));
                tabla.addCell(new PdfPCell(new Phrase(item.getTratamiento(), fontFila)));
                tabla.addCell(new PdfPCell(new Phrase(item.getObservaciones(), fontFila)));
            }

            documento.add(tabla);

            // Gráfico simple: cantidad por servicio
            Paragraph subtituloGrafico = new Paragraph("Resumen Visual de Servicios", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            subtituloGrafico.setSpacingBefore(20);
            subtituloGrafico.setSpacingAfter(10);
            documento.add(subtituloGrafico);

            Image grafico = generarGraficoCircular(historial);
            if (grafico != null) {
                grafico.scaleToFit(400, 400);
                grafico.setAlignment(Image.ALIGN_CENTER);
                documento.add(grafico);
            }

            // Firma
            Paragraph firma = new Paragraph("\n\n________________________\nFirma del Veterinario", fontFila);
            firma.setAlignment(Element.ALIGN_RIGHT);
            firma.setSpacingBefore(40);
            documento.add(firma);

            documento.close();
            writer.close();
            return rutaArchivo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Image generarGraficoCircular(List<HistorialMedicoMascotaDTO> historial) {
        try {
            java.util.Map<String, Integer> contador = new java.util.HashMap<>();
            for (HistorialMedicoMascotaDTO h : historial) {
                contador.put(h.getServicio(), contador.getOrDefault(h.getServicio(), 0) + 1);
            }

            int width = 500, height = 300;
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g = image.createGraphics();

            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, width, height);

            int total = contador.values().stream().mapToInt(i -> i).sum();
            int startAngle = 0;

            java.util.List<java.awt.Color> colores = java.util.List.of(
                java.awt.Color.BLUE, java.awt.Color.GREEN, java.awt.Color.ORANGE,
                java.awt.Color.MAGENTA, java.awt.Color.CYAN, java.awt.Color.RED
            );

            int i = 0;
            for (var entry : contador.entrySet()) {
                int angle = (int) Math.round(360.0 * entry.getValue() / total);
                g.setColor(colores.get(i % colores.size()));
                g.fillArc(50, 50, 200, 200, startAngle, angle);
                g.drawString(entry.getKey() + " (" + entry.getValue() + ")", 280, 70 + i * 20);
                startAngle += angle;
                i++;
            }

            g.dispose();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "png", baos);
            return Image.getInstance(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
