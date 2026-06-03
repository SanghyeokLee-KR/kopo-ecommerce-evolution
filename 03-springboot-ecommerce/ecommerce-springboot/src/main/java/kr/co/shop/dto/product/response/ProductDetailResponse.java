package kr.co.shop.dto.product.response;

/**
 * 상품 상세 DTO
 */
public record ProductDetailResponse(
        Long nbProduct,
        String nmProduct,
        String dcProduct,
        Integer qtPrice,
        Integer qtStock,
        String dtSellStart,
        String stProduct,
        String imagePath,
        Long nbCategory
) {
}