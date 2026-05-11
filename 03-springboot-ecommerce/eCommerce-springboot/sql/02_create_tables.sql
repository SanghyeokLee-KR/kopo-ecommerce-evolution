-- ---------------------------------------------------------
-- [CREATE] 테이블 및 시퀀스 생성
-- ---------------------------------------------------------

-- 1. 회원 (이메일을 로그인 ID로 사용)
CREATE TABLE USERS (
                       USER_NO NUMBER PRIMARY KEY,                   -- PK
                       USER_ID VARCHAR2(15) NOT NULL UNIQUE,         -- 아이디
                       USER_NAME VARCHAR2(50) NOT NULL,              -- 이름
                       USER_PASSWORD VARCHAR2(100) NOT NULL,         -- 비밀번호(암호화)
                       USER_PHONE VARCHAR2(20),                      -- 연락처
                       USER_EMAIL VARCHAR2(100) NOT NULL UNIQUE,     -- 로그인 이메일
                       USER_STATUS VARCHAR2(20) DEFAULT 'ACTIVE' NOT NULL, -- 상태 (ACTIVE, STOPPED, WITHDRAWN)
                       USER_ROLE VARCHAR2(20) DEFAULT 'USER' NOT NULL,     -- 권한 (USER, ADMIN)
                       CREATED_AT DATE DEFAULT SYSDATE NOT NULL,
                       UPDATED_AT DATE
);

CREATE SEQUENCE SEQ_USERS START WITH 1 INCREMENT BY 1;


-- 2. 카테고리
-- PARENT_CATEGORY_NO가 null이면 1뎁스(대분류)
CREATE TABLE CATEGORIES (
                            CATEGORY_NO NUMBER PRIMARY KEY,
                            PARENT_CATEGORY_NO NUMBER,                    -- 상위 카테고리 FK
                            CATEGORY_ID VARCHAR2(20) NOT NULL UNIQUE,     -- 카테고리 코드
                            CATEGORY_NAME VARCHAR2(50) NOT NULL,
                            SORT_ORDER NUMBER DEFAULT 0 NOT NULL,         -- 노출 순서
                            CREATED_AT DATE DEFAULT SYSDATE NOT NULL,
                            UPDATED_AT DATE,

                            CONSTRAINT FK_CATEGORY_PARENT
                                FOREIGN KEY (PARENT_CATEGORY_NO)
                                    REFERENCES CATEGORIES (CATEGORY_NO)
);

CREATE SEQUENCE SEQ_CATEGORIES START WITH 1 INCREMENT BY 1;


-- 3. 상품 마스터
CREATE TABLE PRODUCTS (
                          PRODUCT_NO NUMBER PRIMARY KEY,
                          PRODUCT_ID VARCHAR2(20) NOT NULL UNIQUE,      -- 상품코드
                          PRODUCT_NAME VARCHAR2(100) NOT NULL,
                          PRODUCT_DESC VARCHAR2(1000),                  -- 상세설명
                          PRICE NUMBER NOT NULL,                        -- 판매가
                          STOCK_QTY NUMBER DEFAULT 0 NOT NULL,          -- 재고
                          PRODUCT_STATUS VARCHAR2(20) DEFAULT 'ON_SALE' NOT NULL, -- 상태 (ON_SALE, SOLD_OUT, STOPPED, DELETED)
                          CREATED_AT DATE DEFAULT SYSDATE NOT NULL,
                          UPDATED_AT DATE
);

CREATE SEQUENCE SEQ_PRODUCTS START WITH 1 INCREMENT BY 1;


-- 4. 상품 이미지
CREATE TABLE PRODUCT_IMAGES (
                                IMAGE_NO NUMBER PRIMARY KEY,
                                PRODUCT_NO NUMBER NOT NULL,
                                ORIGINAL_NAME VARCHAR2(255) NOT NULL,         -- 원본파일명
                                STORED_NAME VARCHAR2(255) NOT NULL,           -- 서버저장 파일명
                                IMAGE_PATH VARCHAR2(500) NOT NULL,            -- 물리 경로
                                IMAGE_URL VARCHAR2(500),                      -- 웹 접근 URL
                                IS_MAIN CHAR(1) DEFAULT 'N' NOT NULL,         -- 썸네일(대표) 여부 (Y/N)
                                SORT_ORDER NUMBER DEFAULT 0 NOT NULL,
                                CREATED_AT DATE DEFAULT SYSDATE NOT NULL,

                                CONSTRAINT FK_PRODUCT_IMAGE_PRODUCT
                                    FOREIGN KEY (PRODUCT_NO)
                                        REFERENCES PRODUCTS (PRODUCT_NO),

                                CONSTRAINT CK_PRODUCT_IMAGE_MAIN
                                    CHECK (IS_MAIN IN ('Y', 'N'))
);

CREATE SEQUENCE SEQ_PRODUCT_IMAGES START WITH 1 INCREMENT BY 1;


-- 5. 상품-카테고리 매핑 (N:M 관계 해소용)
CREATE TABLE PRODUCT_CATEGORY_MAPPING (
                                          MAPPING_NO NUMBER PRIMARY KEY,
                                          PRODUCT_NO NUMBER NOT NULL,
                                          CATEGORY_NO NUMBER NOT NULL,
                                          SORT_ORDER NUMBER DEFAULT 0 NOT NULL,         -- 카테고리 내 정렬순서

                                          CONSTRAINT FK_PCM_PRODUCT
                                              FOREIGN KEY (PRODUCT_NO)
                                                  REFERENCES PRODUCTS (PRODUCT_NO),

                                          CONSTRAINT FK_PCM_CATEGORY
                                              FOREIGN KEY (CATEGORY_NO)
                                                  REFERENCES CATEGORIES (CATEGORY_NO),

                                          CONSTRAINT UK_PCM_PRODUCT_CATEGORY
                                              UNIQUE (PRODUCT_NO, CATEGORY_NO)
);

CREATE SEQUENCE SEQ_PRODUCT_CATEGORY_MAPPING START WITH 1 INCREMENT BY 1;


-- 6. 장바구니
CREATE TABLE CARTS (
                       CART_NO NUMBER PRIMARY KEY,
                       USER_NO NUMBER NOT NULL,
                       PRODUCT_NO NUMBER NOT NULL,
                       QUANTITY NUMBER DEFAULT 1 NOT NULL,           -- 수량
                       CREATED_AT DATE DEFAULT SYSDATE NOT NULL,
                       UPDATED_AT DATE,

                       CONSTRAINT FK_CART_USER
                           FOREIGN KEY (USER_NO)
                               REFERENCES USERS (USER_NO),

                       CONSTRAINT FK_CART_PRODUCT
                           FOREIGN KEY (PRODUCT_NO)
                               REFERENCES PRODUCTS (PRODUCT_NO),

                       CONSTRAINT UK_CART_USER_PRODUCT
                           UNIQUE (USER_NO, PRODUCT_NO),

                       CONSTRAINT CK_CART_QUANTITY
                           CHECK (QUANTITY > 0)
);

CREATE SEQUENCE SEQ_CARTS START WITH 1 INCREMENT BY 1;


-- 7. 주문 마스터
CREATE TABLE ORDERS (
                        ORDER_NO NUMBER PRIMARY KEY,
                        USER_NO NUMBER NOT NULL,
                        ORDER_STATUS VARCHAR2(20) DEFAULT 'ORDERED' NOT NULL, -- 상태 (ORDERED, CANCELED 등)
                        TOTAL_PRICE NUMBER NOT NULL,                  -- 결제총액
                        ORDER_DATE DATE DEFAULT SYSDATE NOT NULL,     -- 결제일시
                        CANCELED_AT DATE,                             -- 취소일시

                        CONSTRAINT FK_ORDER_USER
                            FOREIGN KEY (USER_NO)
                                REFERENCES USERS (USER_NO)
);

CREATE SEQUENCE SEQ_ORDERS START WITH 1 INCREMENT BY 1;


-- 8. 주문 상세 (상품 단가 변동에 대비해 주문 당시 데이터를 스냅샷으로 저장)
CREATE TABLE ORDER_ITEMS (
                             ORDER_ITEM_NO NUMBER PRIMARY KEY,
                             ORDER_NO NUMBER NOT NULL,
                             PRODUCT_NO NUMBER,
                             PRODUCT_NAME VARCHAR2(100) NOT NULL,          -- 구매 당시 상품명
                             ORDER_PRICE NUMBER NOT NULL,                  -- 구매 당시 가격
                             QUANTITY NUMBER NOT NULL,
                             ITEM_TOTAL_PRICE NUMBER NOT NULL,             -- 단가 * 수량

                             CONSTRAINT FK_ORDER_ITEM_ORDER
                                 FOREIGN KEY (ORDER_NO)
                                     REFERENCES ORDERS (ORDER_NO),

                             CONSTRAINT FK_ORDER_ITEM_PRODUCT
                                 FOREIGN KEY (PRODUCT_NO)
                                     REFERENCES PRODUCTS (PRODUCT_NO),

                             CONSTRAINT CK_ORDER_ITEM_QUANTITY
                                 CHECK (QUANTITY > 0)
);

CREATE SEQUENCE SEQ_ORDER_ITEMS START WITH 1 INCREMENT BY 1;


-- 9. 상품 댓글 (QnA, 리뷰 등)
-- PARENT_COMMENT_NO가 null이면 최상위 댓글, 값이 있으면 대댓글
CREATE TABLE PRODUCT_COMMENTS (
                                  COMMENT_NO NUMBER PRIMARY KEY,
                                  PRODUCT_NO NUMBER NOT NULL,
                                  USER_NO NUMBER NOT NULL,                      -- 작성자
                                  PARENT_COMMENT_NO NUMBER,                     -- 상위 댓글 번호
                                  CONTENT VARCHAR2(1000) NOT NULL,              -- 내용
                                  COMMENT_STATUS VARCHAR2(20) DEFAULT 'ACTIVE' NOT NULL, -- ACTIVE, DELETED
                                  CREATED_AT DATE DEFAULT SYSDATE NOT NULL,
                                  UPDATED_AT DATE,

                                  CONSTRAINT FK_COMMENT_PRODUCT
                                      FOREIGN KEY (PRODUCT_NO)
                                          REFERENCES PRODUCTS (PRODUCT_NO),

                                  CONSTRAINT FK_COMMENT_USER
                                      FOREIGN KEY (USER_NO)
                                          REFERENCES USERS (USER_NO),

                                  CONSTRAINT FK_COMMENT_PARENT
                                      FOREIGN KEY (PARENT_COMMENT_NO)
                                          REFERENCES PRODUCT_COMMENTS (COMMENT_NO)
);

CREATE SEQUENCE SEQ_PRODUCT_COMMENTS START WITH 1 INCREMENT BY 1;