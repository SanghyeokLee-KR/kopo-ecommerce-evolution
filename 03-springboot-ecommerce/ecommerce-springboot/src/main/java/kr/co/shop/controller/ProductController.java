package kr.co.shop.controller;

import kr.co.shop.dto.product.response.ProductListResponse;
import kr.co.shop.service.CategoryService;
import kr.co.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 사용자 상품 조회 Controller
 */
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // 사용자 상품 목록
    @GetMapping("/products")
    public String list(
            @RequestParam(required = false) Long nbCategory,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        Page<ProductListResponse> productPage = productService.findDisplayProducts(nbCategory, keyword, page);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategory", nbCategory);
        model.addAttribute("keyword", keyword);

        return "product/list";
    }

    // 사용자 상품 상세
    @GetMapping("/products/{nbProduct}")
    public String detail(@PathVariable Long nbProduct, Model model) {
        model.addAttribute("product", productService.findById(nbProduct));
        return "product/detail";
    }
}