package kr.co.shop.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자 권한 변경 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class UserTypeUpdateRequest {

    @NotBlank(message = "사용자 구분 코드를 선택해주세요.")
    private String cdUserType;
}