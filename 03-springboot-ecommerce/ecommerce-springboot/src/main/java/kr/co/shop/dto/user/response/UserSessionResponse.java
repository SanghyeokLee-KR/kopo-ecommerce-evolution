package kr.co.shop.dto.user.response;

import java.io.Serializable;

/**
 * 세션 저장용 사용자 DTO
 */
public record UserSessionResponse(
        Long nbUser,
        String idUser,
        String nmUser,
        String nmEmail,
        String stStatus,
        String cdUserType
) implements Serializable {
}