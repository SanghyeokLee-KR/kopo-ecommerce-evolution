package kr.co.shop.dto.admin.response;

/**
 * 관리자 대시보드 응답 DTO
 */
public record AdminDashboardResponse(
        long userCount,
        long categoryCount,
        long productCount,
        long orderCount
) {

}