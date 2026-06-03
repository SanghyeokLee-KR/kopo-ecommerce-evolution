package kr.co.shop.common.enums;

import lombok.Getter;

/**
 * 상품 상태 코드
 */
@Getter
public enum ProductStatus {

    ON_SALE("PR01", "판매중"),
    SOLD_OUT("PR02", "품절");

    private final String code;
    private final String description;

    ProductStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}