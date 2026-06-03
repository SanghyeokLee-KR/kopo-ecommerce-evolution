package kr.co.shop.common.enums;

import lombok.Getter;

/**
 * 주문 상태 코드 enum.
 */
@Getter
public enum OrderStatus {

    ORDER_COMPLETE("OR01", "주문완료"),
    PREPARING("OR02", "배송준비"),
    SHIPPING("OR03", "배송중"),
    DELIVERY_COMPLETE("OR04", "배송완료"),
    CANCELED("OR05", "주문취소");

    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 관리자 API에서 상태 변경 요청이 들어올 때 유효성 체크용
    public static boolean isValid(String code) {
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
}