package kr.co.shop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.shop.common.util.SessionUtil;
import kr.co.shop.dto.user.request.LoginRequest;
import kr.co.shop.dto.user.request.MyPageUpdateRequest;
import kr.co.shop.dto.user.request.PasswordChangeRequest;
import kr.co.shop.dto.user.request.UserSignupRequest;
import kr.co.shop.dto.user.response.MyPageResponse;
import kr.co.shop.dto.user.response.UserSessionResponse;
import kr.co.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원가입 화면
    @GetMapping("/signup")
    public String signupForm() {
        return "user/signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute UserSignupRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "입력값을 확인해주세요.");
            return "user/signup";
        }

        userService.signup(request);
        return "redirect:/users/login";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest request, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginError", "아이디와 비밀번호를 입력해주세요."); // 이거 쓸모 없어짐 글로벌 핸들러 사용해서
            return "user/login";
        }

        UserSessionResponse loginUser = userService.login(request);
        SessionUtil.setLoginUser(session, loginUser);

        return "redirect:/";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessionUtil.logout(session);
        return "redirect:/users/login";
    }

    // 마이페이지 조회
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        MyPageResponse response = userService.getMyPage(loginUser.nbUser());

        model.addAttribute("myPage", response);
        return "user/mypage";
    }

    // 회원정보 수정 화면
    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        MyPageResponse response = userService.getMyPage(loginUser.nbUser());

        model.addAttribute("myPage", response);
        return "user/edit";
    }

    // 회원정보 수정 처리
    @PostMapping("/edit")
    public String updateEdit(HttpSession session, @Valid @ModelAttribute MyPageUpdateRequest request, BindingResult bindingResult, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);

        if (bindingResult.hasErrors()) {
            model.addAttribute("myPage", userService.getMyPage(loginUser.nbUser()));
            model.addAttribute("errorMessage", "입력값을 확인해주세요.");
            return "user/edit";
        }

        userService.updateMyPage(loginUser.nbUser(), request);
        return "redirect:/users/mypage";
    }

    // 비밀번호 변경
    @PostMapping("/password")
    public String changePassword(HttpSession session, @Valid @ModelAttribute PasswordChangeRequest request, BindingResult bindingResult, Model model) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);

        if (bindingResult.hasErrors()) {
            model.addAttribute("myPage", userService.getMyPage(loginUser.nbUser()));
            model.addAttribute("errorMessage", "비밀번호 입력값을 확인해주세요.");
            return "user/mypage";
        }

        userService.changePassword(loginUser.nbUser(), request);
        return "redirect:/users/mypage";
    }

    // 회원 탈퇴 요청
    @PostMapping("/withdraw")
    public String requestWithdraw(HttpSession session) {
        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);
        userService.requestWithdraw(loginUser.nbUser());

        SessionUtil.logout(session);
        return "redirect:/";
    }
}