package com.puenteblanco.pb.controller.vet.view;

import com.puenteblanco.pb.dto.dashboard.DashboardVetName;
import com.puenteblanco.pb.services.interfaces.vetDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor

public class VetDashboardViewController {
      private final vetDashboardService vetDashboardService;

    @GetMapping("/vet/dashboard")
    public String showVetDashboard(Authentication authentication, Model model) {
        DashboardVetName vetName = vetDashboardService.getVetName(authentication);
        model.addAttribute("vetName", vetName);
        return "vet/veterinarian_dashboard";
    }
    
}
