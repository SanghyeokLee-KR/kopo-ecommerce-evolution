package kr.co.shop.controller.admin;

import jakarta.validation.Valid;
import kr.co.shop.dto.category.request.CategoryCreateRequest;
import kr.co.shop.dto.category.request.CategoryUpdateRequest;
import kr.co.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 관리자 카테고리 관리 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    // 카테고리 목록 조회
    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/category/list";
    }

    // 카테고리 등록
    @PostMapping
    public String create(@Valid @ModelAttribute CategoryCreateRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "카테고리 입력값을 확인해주세요.");
            return "redirect:/admin/categories";
        }

        categoryService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "카테고리가 등록되었습니다.");
        return "redirect:/admin/categories";
    }

    // 카테고리 수정
    @PutMapping("/{nbCategory}")
    public String update(@PathVariable Long nbCategory, @Valid @ModelAttribute CategoryUpdateRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "카테고리 수정값을 확인해주세요.");
            return "redirect:/admin/categories";
        }

        categoryService.update(nbCategory, request);
        redirectAttributes.addFlashAttribute("successMessage", "카테고리가 수정되었습니다.");
        return "redirect:/admin/categories";
    }

    // 카테고리 삭제
    @PostMapping("/{nbCategory}/delete")
    public String delete(@PathVariable Long nbCategory, RedirectAttributes redirectAttributes) {
        categoryService.delete(nbCategory);
        redirectAttributes.addFlashAttribute("successMessage", "카테고리가 삭제되었습니다.");
        return "redirect:/admin/categories";
    }
}