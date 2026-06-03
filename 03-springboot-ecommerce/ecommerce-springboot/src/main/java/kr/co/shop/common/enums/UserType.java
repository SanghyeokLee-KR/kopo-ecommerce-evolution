package kr.co.shop.common.enums;

import lombok.Getter;

/**
 * 사용자 구분 코드
 */
@Getter
public enum UserType {

    USER("10", "일반사용자"),
    ADMIN("20", "관리자");

    private final String code;
    private final String description;

    UserType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}