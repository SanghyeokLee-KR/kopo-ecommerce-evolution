package kr.co.shop.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String idUser;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String nmPaswd;
}