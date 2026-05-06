package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.response.AdminAlertResponseDto;
import com.puenteblanco.pb.dto.response.AdminMonthlyRevenueResponseDto;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.PagoRepository;
import com.puenteblanco.pb.services.interfaces.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;
    private static final String[] MESES = {
        "Ene", "Feb", "Mar", "Abr", "May", "Jun",
        "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
    };

    @Override
    public int countTodayAppointments() {
        LocalDate today = LocalDate.now();
        return citaRepository.countByFecha(today);
    }

    @Override
public double getWeeklyRevenue() {
    LocalDate today = LocalDate.now();
    LocalDate start = today.with(java.time.DayOfWeek.MONDAY);
    LocalDate end = today.with(java.time.DayOfWeek.SUNDAY);

    return pagoRepository.getTotalPagadoEntreFechas(
            start.atStartOfDay(),
            end.atTime(23, 59, 59));
}

    @Override
    public int countWeeklyCancellations() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);
        return citaRepository.countByEstadoAndFechaBetween("CANCELADA", startOfWeek, endOfWeek);
    }

    @Override
public List<AdminMonthlyRevenueResponseDto> getMonthlyRevenue() {
    int anioActual = LocalDate.now().getYear();
    List<Object[]> resultados = pagoRepository.getIngresosPorMes(anioActual);

    return resultados.stream().map(obj -> {
    int mesIndex = ((Number) obj[0]).intValue();
    double monto = ((Number) obj[1]).doubleValue();
    return new AdminMonthlyRevenueResponseDto(MESES[mesIndex - 1], monto);
}).toList();
}

    @Override
public List<AdminAlertResponseDto> getRecentAlerts() {
    List<AdminAlertResponseDto> alerts = new ArrayList<>();

    // 1. Citas COMPLETADAS sin AtencionMedica registrada
    int sinAtencion = citaRepository.countCompletadasSinAtencion();
    if (sinAtencion > 0) {
        alerts.add(new AdminAlertResponseDto(
            "warning",
            "Citas sin Pago completado",
            sinAtencion + " citas completadas no han sido aún pagadas."
        ));
    }

    // 2. Citas EVALUADAS pendientes de validación por veterinario
    int evaluadas = citaRepository.countByEstado("EVALUADA");
    if (evaluadas > 0) {
        alerts.add(new AdminAlertResponseDto(
            "info",
            "Citas evaluadas sin validar",
            evaluadas + " citas han sido atendidas por internos y esperan validación."
        ));
    }

    // Más alertas se pueden agregar aquí...

    return alerts;
}
}
