package kr.co.shop.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

// 이거 전 프로젝트에서 깃허브에서 가져온겁니다.
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 서비스에서 직접 발생시킨 예외 처리
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(
            BusinessException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        // 어떤 URL에서 비즈니스 예외가 발생했는지 로그로 남김
        log.warn("[비즈니스 예외 발생] 요청 경로: {}, 에러 메시지: {}",
                request.getRequestURI(),
                e.getMessage());

        // redirect 후에도 메시지를 유지하기 위해 Flash Attribute 사용
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        // 이전 페이지 주소
        String referer = request.getHeader("Referer");

        // 이전 페이지 정보가 없으면 메인 화면으로 이동
        if (!StringUtils.hasText(referer)) {
            return "redirect:/";
        }

        // 오류가 발생한 이전 페이지로 다시 이동
        return "redirect:" + referer;
    }

    // @Valid 검증 실패 처리 (폼 입력값 오류)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(
            MethodArgumentNotValidException e,
            Model model
    ) {
        // 첫 번째 검증 오류 메시지만 화면에 표시
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값을 확인해주세요.");

        log.warn("[입력값 검증 실패] 사유: {}", message);

        model.addAttribute("errorMessage", message);

        return "common/error";
    }

    // @Validated 검증 실패 처리 (URL 파라미터 등)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(
            ConstraintViolationException e,
            Model model
    ) {
        log.warn("[파라미터 제약조건 위반] 상세 내용: {}", e.getMessage());

        model.addAttribute("errorMessage", "요청값이 올바르지 않습니다.");

        return "common/error";
    }

    // 존재하지 않는 정적 리소스 요청 처리 (404)
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(
            NoResourceFoundException e,
            Model model
    ) {
        log.warn("[정적 리소스 없음] {}", e.getMessage());

        model.addAttribute("errorMessage", "요청하신 페이지를 찾을 수 없습니다.");

        return "common/error";
    }

    // 위에서 처리되지 않은 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception e,
            Model model
    ) {
        // 예상하지 못한 시스템 오류는 stack trace까지 남김
        log.error("[시스템 오류 발생]", e);

        model.addAttribute("errorMessage",
                "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");

        return "common/error";
    }
}