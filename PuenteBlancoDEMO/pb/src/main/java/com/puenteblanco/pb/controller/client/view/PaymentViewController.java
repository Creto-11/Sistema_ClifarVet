package com.puenteblanco.pb.controller.client.view;

import com.puenteblanco.pb.services.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PaymentViewController {

    private final DashboardService dashboardService;

    @GetMapping("/payment-form")
    public String showPaymentForm(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            var dashboard = dashboardService.getClientDashboard(authentication);
            model.addAttribute("dashboard", dashboard);
        } else {
            model.addAttribute("dashboard", null);
        }
        return "payment_form";
    }
}
