package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.reportes.CitaPorFechaDTO;
import com.puenteblanco.pb.dto.reportes.CitaPorMascotaDTO;
import com.puenteblanco.pb.dto.reportes.CitaCanceladaDTO;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.services.interfaces.ReportService;
import com.puenteblanco.pb.services.pdf.PdfReportService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final CitaRepository citaRepository;
    private final UserRepository userRepository;
    private final PdfReportService pdfReportService;

    @Override
    public List<CitaPorFechaDTO> obtenerCitasPorFecha(LocalDate startDate, LocalDate endDate) {
        List<Cita> citas = citaRepository.findByFechaBetween(startDate, endDate);

        return citas.stream().map(cita -> new CitaPorFechaDTO(
                cita.getId(),
                cita.getUsuario().getNombres(),
                cita.getPet().getName(),
                cita.getServicio().getDescripcion(),
                cita.getVeterinario().getUsuario().getNombres(),
                cita.getFecha(),
                cita.getHora(),
                cita.getEstado()
        )).collect(Collectors.toList());
    }

    @Override
    public String obtenerNombreVeterinarioPorCorreo(String correo) {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con correo: " + correo));
        return user.getNombres() + " " + user.getApellidoPaterno();
    }

    // --- Citas canceladas ---
    @Override
    public void exportCitasCanceladasReport(OutputStream outputStream, String startDate, String endDate, String emitidoPor) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Cita> citas = citaRepository.findByFechaBetween(start, end);

        List<CitaCanceladaDTO> datos = citas.stream()
                .filter(c -> "CANCELADA".equalsIgnoreCase(c.getEstado()))
                .map(cita -> new CitaCanceladaDTO(
                        cita.getId(),
                        cita.getUsuario().getNombres(),
                        cita.getPet().getName(),
                        cita.getServicio().getDescripcion(),
                        cita.getVeterinario().getUsuario().getNombres(),
                        cita.getFecha(),
                        cita.getHora(),
                        cita.getMotivoCancelacion()
                ))
                .collect(Collectors.toList());

        pdfReportService.generarPdfCitasCanceladas(start, end, outputStream, datos, emitidoPor);
    }

    @Override
public List<CitaPorFechaDTO> obtenerCitasPorFechaFiltrado(LocalDate startDate, LocalDate endDate, String tipoServicio) {
    List<Cita> citas = citaRepository.findByFechaBetween(startDate, endDate);

    return citas.stream()
            .filter(cita -> {
                if (tipoServicio == null || tipoServicio.isBlank()) return true;
                String nombreTipo = cita.getServicio().getTipoServicio().getNombre();
                return nombreTipo.equalsIgnoreCase(tipoServicio);
            })
            .map(cita -> new CitaPorFechaDTO(
                    cita.getId(),
                    cita.getUsuario().getNombres(),
                    cita.getPet().getName(),
                    cita.getServicio().getDescripcion(),
                    cita.getVeterinario().getUsuario().getNombres(),
                    cita.getFecha(),
                    cita.getHora(),
                    cita.getEstado()
            ))
            .collect(Collectors.toList());
}

@Override
public List<CitaPorMascotaDTO> obtenerCitasPorMascotaFiltrado(
    LocalDate startDate,
    LocalDate endDate,
    String tipoMascota,
    String cliente
) {
    List<Cita> citas = citaRepository.findByFechaBetween(startDate, endDate);

    return citas.stream()
        .filter(c -> "COMPLETADA".equalsIgnoreCase(c.getEstado()))
        .filter(c -> tipoMascota == null || tipoMascota.isBlank() || 
                     c.getPet().getType().equalsIgnoreCase(tipoMascota))
        .filter(c -> cliente == null || cliente.isBlank() || 
                     c.getUsuario().getCorreo().equalsIgnoreCase(cliente))
        .map(cita -> new CitaPorMascotaDTO(
            cita.getPet().getName(),
            cita.getPet().getType(),
            cita.getPet().getBreed(),
            cita.getServicio().getDescripcion(),
            cita.getUsuario().getNombres() + " " + cita.getUsuario().getApellidoPaterno(),
            cita.getFecha(),
            cita.getHora(),
            cita.getEstado()
        ))
        .toList();
}

@Override
public void generarPdfCitasPorMascota(LocalDate startDate, LocalDate endDate, OutputStream outputStream, String tipoMascota, String cliente, String emitidoPor) {
    List<CitaPorMascotaDTO> datos = obtenerCitasPorMascotaFiltrado(startDate, endDate, tipoMascota, cliente);
    pdfReportService.generarPdfCitasPorMascota(startDate, endDate, outputStream, datos, emitidoPor);
}

@Override
public void generarPdfCitasPorFecha(LocalDate startDate, LocalDate endDate, OutputStream outputStream, String emitidoPor, String tipoServicio) {
    List<CitaPorFechaDTO> datos;

    if (tipoServicio != null && !tipoServicio.isBlank()) {
        datos = obtenerCitasPorFechaFiltrado(startDate, endDate, tipoServicio);
    } else {
        datos = obtenerCitasPorFecha(startDate, endDate);
    }

    pdfReportService.generarPdfCitasPorFecha(startDate, endDate, outputStream, datos, emitidoPor, tipoServicio);
}
    
}
