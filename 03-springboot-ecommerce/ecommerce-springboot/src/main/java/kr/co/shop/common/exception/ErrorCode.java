package kr.co.shop.common.exception;

import lombok.Getter;

/**
 * 에러 코드
 */
@Getter
public enum ErrorCode {

    // 회원 관련
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATE_USER_ID("이미 사용 중인 아이디입니다."),
    DUPLICATE_EMAIL("이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    NOT_APPROVED_USER("승인되지 않은 회원입니다."),
    LOGIN_REQUIRED("로그인이 필요합니다."),

    // 카테고리 관련
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다."),
    DUPLICATE_CATEGORY_ORDER("같은 위치에 이미 사용 중인 정렬 순서입니다."),

    // 상품 관련
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),
    OUT_OF_STOCK("품절된 상품입니다."),
    NOT_ENOUGH_STOCK("상품 재고가 부족합니다."),

    // 장바구니 관련
    BASKET_NOT_FOUND("장바구니를 찾을 수 없습니다."),
    BASKET_EMPTY("장바구니가 비어 있습니다."),
    BASKET_ITEM_NOT_FOUND("장바구니 상품을 찾을 수 없습니다."),

    // 주문 관련
    ORDER_NOT_FOUND("주문을 찾을 수 없습니다."),
    FORBIDDEN_ORDER_ACCESS("주문 접근 권한이 없습니다."),
    INVALID_ORDER_STATUS("주문 상태값이 올바르지 않습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}