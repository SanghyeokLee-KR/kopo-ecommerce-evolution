package kr.co.shop.controller.admin;

import kr.co.shop.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 관리자 대시보드 Controller
 */
@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    // 관리자 메인 대시보드
    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("dashboard", adminDashboardService.getDashboard());
        return "admin/dashboard";
    }
}