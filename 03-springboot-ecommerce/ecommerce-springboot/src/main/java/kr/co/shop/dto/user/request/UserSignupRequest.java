package kr.co.shop.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원가입 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class UserSignupRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 5, max = 15, message = "아이디는 5~15자로 입력해주세요.")
    private String idUser;

    @NotBlank(message = "이름을 입력해주세요.")
    private String nmUser;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{5,15}$",
            message = "비밀번호는 대문자, 소문자, 숫자를 포함한 5~15자로 입력해주세요."
    )
    private String nmPaswd;

    @NotBlank(message = "휴대전화를 입력해주세요.")
    private String noMobile;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    private String nmEmail;
}