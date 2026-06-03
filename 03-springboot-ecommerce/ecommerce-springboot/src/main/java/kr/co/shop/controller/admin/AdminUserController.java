package kr.co.shop.controller.admin;

import kr.co.shop.dto.user.response.UserListResponse;
import kr.co.shop.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 회원 관리 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 회원 목록 조회
    @GetMapping
    public String list(@RequestParam(required = false) String stStatus, @RequestParam(defaultValue = "1") int page, Model model) {
        Page<UserListResponse> userPage = adminUserService.findUsers(stStatus, page);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("userPage", userPage);
        model.addAttribute("selectedStatus", stStatus);

        return "admin/user/list";
    }

    // 회원 상태 변경
    @PostMapping("/{nbUser}/status")
    public String changeStatus(@PathVariable Long nbUser, @RequestParam String stStatus, @RequestParam(required = false) String selectedStatus, @RequestParam(defaultValue = "1") int page) {
        adminUserService.changeUserStatus(nbUser, stStatus);

        return "redirect:/admin/users?stStatus=" + (selectedStatus == null ? "" : selectedStatus) + "&page=" + page;
    }

    // 회원 권한 변경
    @PostMapping("/{nbUser}/type")
    public String changeType(@PathVariable Long nbUser, @RequestParam String cdUserType, @RequestParam(required = false) String selectedStatus, @RequestParam(defaultValue = "1") int page) {
        adminUserService.changeUserType(nbUser, cdUserType);

        return "redirect:/admin/users?stStatus=" + (selectedStatus == null ? "" : selectedStatus) + "&page=" + page;
    }
}