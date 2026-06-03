package kr.co.shop.dto.product.response;

/**
 * 상품 목록 DTO
 */
public record ProductListResponse(
        Long nbProduct,
        String nmProduct,
        Integer qtPrice,
        Integer qtStock,
        String dtSellStart,
        String stProduct,
        String imagePath
) {
}