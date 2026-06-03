package kr.co.shop.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.shop.common.util.SessionUtil;
import kr.co.shop.dto.order.response.OrderCompleteResponse;
import kr.co.shop.dto.order.response.OrderDetailResponse;
import kr.co.shop.dto.order.response.OrderListResponse;
import kr.co.shop.dto.user.response.UserSessionResponse;
import kr.co.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주문 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 장바구니 상품 전체 주문
    @PostMapping
    public String orderFromBasket(HttpSession session, @RequestParam(required = false) List<Long> nbBasketItemIds, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);

        OrderCompleteResponse order = orderService.orderFromBasket(loginUser.nbUser(), nbBasketItemIds);

        model.addAttribute("order", order);
        return "order/complete";
    }

    // 상품 바로 주문
    @PostMapping("/direct")
    public String orderDirect(HttpSession session, @RequestParam Long nbProduct, @RequestParam(defaultValue = "1") int quantity, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);

        OrderCompleteResponse order = orderService.orderDirect(loginUser.nbUser(), nbProduct, quantity);

        model.addAttribute("order", order);

        return "order/complete";
    }

    // 내 주문 목록 조회
    @GetMapping
    public String orderList(HttpSession session, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        List<OrderListResponse> orders = orderService.findMyOrders(loginUser.nbUser());

        model.addAttribute("orders", orders);
        return "order/list";
    }

    // 내 주문 상세 조회
    @GetMapping("/{nbOrder}")
    public String orderDetail(@PathVariable Long nbOrder, HttpSession session, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        OrderDetailResponse order = orderService.findMyOrderDetail(loginUser.nbUser(), nbOrder);

        model.addAttribute("order", order);
        return "order/detail";
    }
}