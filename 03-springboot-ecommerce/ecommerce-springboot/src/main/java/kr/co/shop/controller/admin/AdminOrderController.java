package kr.co.shop.controller.admin;

import kr.co.shop.dto.admin.response.AdminOrderListResponse;
import kr.co.shop.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 주문 관리 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    // 관리자 주문 목록 조회
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Page<AdminOrderListResponse> orderPage =
                adminOrderService.findAllOrders(page, startDate, endDate, keyword);

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("orders", orderPage.getContent());

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("keyword", keyword);

        return "admin/order/list";
    }

    // 주문 상세 조회
    @GetMapping("/{nbOrder}")
    public String detail(@PathVariable Long nbOrder, Model model) {
        model.addAttribute("order", adminOrderService.findOrderDetail(nbOrder));
        return "admin/order/detail";
    }

    // 주문 상태 변경
    @PostMapping("/{nbOrder}/status")
    public String changeStatus(@PathVariable Long nbOrder, @RequestParam String stOrder) {
        adminOrderService.changeOrderStatus(nbOrder, stOrder);
        return "redirect:/admin/orders/" + nbOrder;
    }
}