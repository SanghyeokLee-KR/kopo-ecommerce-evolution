package kr.co.shop.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.shop.common.util.SessionUtil;
import kr.co.shop.dto.basket.response.BasketResponse;
import kr.co.shop.dto.user.response.UserSessionResponse;
import kr.co.shop.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 장바구니 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    // 장바구니 목록 조회
    @GetMapping
    public String basket(HttpSession session, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        BasketResponse basket = basketService.getBasket(loginUser.nbUser());

        model.addAttribute("basket", basket);
        return "basket/list";
    }

    // 장바구니에 상품 추가
    @PostMapping("/items")
    public String addItem(HttpSession session, @RequestParam Long nbProduct, @RequestParam(defaultValue = "1") int quantity) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        basketService.addItem(loginUser.nbUser(), nbProduct, quantity);

        return "redirect:/basket";
    }

    // 장바구니 상품 수량 변경
    @PostMapping("/items/{nbBasketItem}/quantity")
    public String updateQuantity(HttpSession session, @PathVariable Long nbBasketItem, @RequestParam int quantity) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        basketService.updateQuantity(loginUser.nbUser(), nbBasketItem, quantity);

        return "redirect:/basket";
    }

    // 장바구니 상품 삭제
    @PostMapping("/items/{nbBasketItem}/delete")
    public String removeItem(HttpSession session, @PathVariable Long nbBasketItem) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        basketService.removeItem(loginUser.nbUser(), nbBasketItem);

        return "redirect:/basket";
    }

    // 장바구니 전체 비우기
    @PostMapping("/clear")
    public String clearBasket(HttpSession session) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        basketService.clearBasket(loginUser.nbUser());

        return "redirect:/basket";
    }
}