-- =========================================================
-- 관리자 계정 초기 데이터
-- 비밀번호는 Spring Security BCrypt 암호화 값으로 변경 필요
-- 현재 값은 임시 문자열이다.
-- =========================================================

INSERT INTO USERS (
    USER_NO,
    USER_ID,
    USER_NAME,
    USER_PASSWORD,
    USER_PHONE,
    USER_EMAIL,
    USER_STATUS,
    USER_ROLE,
    CREATED_AT
) VALUES (
             SEQ_USERS.NEXTVAL,
             'admin',
             '관리자',
             '$2a$10$TEMP_PASSWORD_CHANGE_ME',
             '010-0000-0000',
             'admin@shop.com',
             'ACTIVE',
             'ADMIN',
             SYSDATE
         );

COMMIT;