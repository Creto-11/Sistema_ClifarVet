package com.puenteblanco.pb.controller.admin;

import com.puenteblanco.pb.dto.response.AdminAlertResponseDto;
import com.puenteblanco.pb.dto.response.AdminMonthlyRevenueResponseDto;
import com.puenteblanco.pb.services.interfaces.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/appointments/today")
    public ResponseEntity<Integer> getTodayAppointments() {
        int count = adminDashboardService.countTodayAppointments();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/revenue/week")
    public ResponseEntity<Double> getWeeklyRevenue() {
        double revenue = adminDashboardService.getWeeklyRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/cancellations/week")
    public ResponseEntity<Integer> getWeeklyCancellations() {
        int cancellations = adminDashboardService.countWeeklyCancellations();
        return ResponseEntity.ok(cancellations);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<AdminMonthlyRevenueResponseDto>> getMonthlyRevenue() {
        List<AdminMonthlyRevenueResponseDto> revenueList = adminDashboardService.getMonthlyRevenue();
        return ResponseEntity.ok(revenueList);
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<AdminAlertResponseDto>> getRecentAlerts() {
        List<AdminAlertResponseDto> alerts = adminDashboardService.getRecentAlerts();
        return ResponseEntity.ok(alerts);
    }
}
