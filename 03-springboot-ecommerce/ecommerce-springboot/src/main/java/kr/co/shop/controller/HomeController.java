package kr.co.shop.controller;

import org.springframework.ui.Model;
import kr.co.shop.dto.product.response.ProductListResponse;
import kr.co.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 메인 페이지 Controller
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        // 최신순으로 정렬된 상품 3개 조회
        List<ProductListResponse> products = productService.findLatestProducts(3);

        model.addAttribute("products", products);

        return "index";

    }
}

