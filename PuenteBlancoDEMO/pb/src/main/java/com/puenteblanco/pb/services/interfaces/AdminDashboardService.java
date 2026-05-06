package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.response.AdminAlertResponseDto;
import com.puenteblanco.pb.dto.response.AdminMonthlyRevenueResponseDto;

import java.util.List;

public interface AdminDashboardService {

    int countTodayAppointments();

    double getWeeklyRevenue();

    int countWeeklyCancellations();

    List<AdminMonthlyRevenueResponseDto> getMonthlyRevenue();

    List<AdminAlertResponseDto> getRecentAlerts();
}
