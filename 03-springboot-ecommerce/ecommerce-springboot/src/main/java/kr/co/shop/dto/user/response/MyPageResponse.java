package kr.co.shop.dto.user.response;

/**
 * 마이페이지 조회 DTO
 */
public record MyPageResponse(
        String idUser,
        String nmUser,
        String noMobile,
        String nmEmail
) {
}