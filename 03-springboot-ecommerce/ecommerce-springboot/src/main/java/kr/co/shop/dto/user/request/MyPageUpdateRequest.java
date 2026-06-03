package kr.co.shop.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 마이페이지 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class MyPageUpdateRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String nmUser;

    @NotBlank(message = "휴대전화를 입력해주세요.")
    private String noMobile;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    private String nmEmail;
}