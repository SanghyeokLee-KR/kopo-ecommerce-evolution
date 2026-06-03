package kr.co.shop.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자 상태 변경 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class UserStatusUpdateRequest {

    @NotBlank(message = "상태 코드를 선택해주세요.")
    private String stStatus;
}