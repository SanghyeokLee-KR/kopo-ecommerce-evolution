package kr.co.shop.controller.admin;

import jakarta.validation.Valid;
import kr.co.shop.dto.product.request.ProductRequest;
import kr.co.shop.dto.product.response.ProductDetailResponse;
import kr.co.shop.dto.product.response.ProductListResponse;
import kr.co.shop.service.CategoryService;
import kr.co.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 관리자 상품 관리 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // 관리자 상품 목록 조회
    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        Page<ProductListResponse> productPage = productService.findAdminProducts(page);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("categories", categoryService.findAll());

        return "admin/product/list";
    }

    // 상품 등록 처리
    @PostMapping
    public String create(@Valid @ModelAttribute ProductRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "상품 입력값을 확인해주세요.");
            return "redirect:/admin/products";
        }

        productService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "상품이 등록되었습니다.");
        return "redirect:/admin/products";
    }

    // 상품 수정 화면
    @GetMapping("/{nbProduct}/edit")
    public String editForm(@PathVariable Long nbProduct, Model model) {
        ProductDetailResponse product = productService.findById(nbProduct);

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());

        return "admin/product/edit";
    }

    // 상품 수정 처리
    @PostMapping("/{nbProduct}/edit")
    public String update(@PathVariable Long nbProduct, @Valid @ModelAttribute ProductRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "상품 수정값을 확인해주세요.");
            return "redirect:/admin/products/" + nbProduct + "/edit";
        }

        productService.update(nbProduct, request);
        redirectAttributes.addFlashAttribute("successMessage", "상품이 수정되었습니다.");
        return "redirect:/admin/products";
    }

    // 상품 삭제 처리
    @PostMapping("/{nbProduct}/delete")
    public String delete(@PathVariable Long nbProduct, RedirectAttributes redirectAttributes) {
        productService.delete(nbProduct);
        redirectAttributes.addFlashAttribute("successMessage", "상품이 삭제되었습니다.");
        return "redirect:/admin/products";
    }
}