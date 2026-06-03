package kr.co.shop.dto.category.response;

/**
 * 카테고리 응답 DTO
 */
public record CategoryResponse(
        Long nbCategory,
        Long nbParentCategory,
        String nmCategory,
        Integer cnLevel,
        Integer cnOrder
) {
}