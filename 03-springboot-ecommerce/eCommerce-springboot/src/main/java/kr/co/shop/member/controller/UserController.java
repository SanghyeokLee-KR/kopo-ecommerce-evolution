package kr.co.shop.member.controller;

import jakarta.validation.Valid;
import kr.co.shop.member.dto.request.UserJoinRequest;
import kr.co.shop.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 회원 관련 화면을 처리하는 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원가입 화면
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "member/join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid UserJoinRequest userJoinRequest,
                       BindingResult bindingResult) {

        // 입력값 검증 실패
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            userService.join(userJoinRequest);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("joinFail", e.getMessage());
            return "member/join";
        }

        // 회원가입 완료 후 로그인 페이지로 이동
        return "redirect:/login";
    }
}