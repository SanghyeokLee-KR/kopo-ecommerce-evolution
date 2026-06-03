package kr.co.shop.dto.user.response;

/**
 * 관리자 사용자 목록 DTO
 */
public record UserListResponse(
        Long nbUser,
        String idUser,
        String nmUser,
        String noMobile,
        String nmEmail,
        String stStatus,
        String cdUserType
) {
}