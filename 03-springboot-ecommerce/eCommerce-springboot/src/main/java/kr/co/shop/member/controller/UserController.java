package kr.co.shop.member.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import kr.co.shop.member.dto.UserDto;
import kr.co.shop.member.entity.User;
import kr.co.shop.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 회원 관련 화면을 처리하는 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원가입 화면
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("UserDto", new UserDto());
        return "member/join";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("UserDto", new UserDto());
        return "member/login";
    }

    @PostMapping("/join")
    public String join(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            userService.join(userDto);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("joinFail", e.getMessage());
            return "member/join";
        }

        return "redirect:/users/login";
    }

    @PostMapping("/login")
    public String login(UserDto userDto,
                        HttpSession session,
                        Model model) {
        System.out.println("이메일 주소 -> "+userDto.getUserEmail());
        System.out.println("패스워드 -> " + userDto.getUserPassword());

        User loginUser = userService.login(userDto.getUserEmail(), userDto.getUserPassword());

        if (loginUser == null) {
            model.addAttribute("loginError", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "member/login";
        }

        System.out.println("로그인 성공");
        session.setAttribute("loginUser", loginUser);
        return "redirect:/";
    }
}