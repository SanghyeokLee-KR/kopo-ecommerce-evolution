-- =========================================================
-- 테스트용 샘플 데이터
-- =========================================================

-- 대분류 카테고리
INSERT INTO CATEGORIES (
    CATEGORY_NO,
    PARENT_CATEGORY_NO,
    CATEGORY_ID,
    CATEGORY_NAME,
    SORT_ORDER,
    CREATED_AT
) VALUES (
             SEQ_CATEGORIES.NEXTVAL,
             NULL,
             'ELEC',
             '전자제품',
             1,
             SYSDATE
         );

-- 일반 사용자
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
             'user01',
             '테스트사용자',
             '$2a$10$TEMP_PASSWORD_CHANGE_ME',
             '010-1111-1111',
             'user01@shop.com',
             'ACTIVE',
             'USER',
             SYSDATE
         );

COMMIT;