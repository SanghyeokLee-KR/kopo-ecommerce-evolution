package kr.co.shop.common.enums;

import lombok.Getter;

/**
 * 사용자 상태 코드
 */
@Getter
public enum UserStatus {

    NORMAL("ST01", "정상"),
    WITHDRAWN("ST02", "해지"),
    JOIN_REQUEST("ST03", "가입요청"),
    WITHDRAW_REQUEST("ST04", "탈퇴요청");

    private final String code;
    private final String description;

    UserStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}