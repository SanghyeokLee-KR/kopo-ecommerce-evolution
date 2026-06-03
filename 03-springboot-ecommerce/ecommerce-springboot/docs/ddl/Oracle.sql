DROP TABLE tb_order_item CASCADE CONSTRAINTS;
DROP TABLE tb_order CASCADE CONSTRAINTS;
DROP TABLE tb_basket_item CASCADE CONSTRAINTS;
DROP TABLE tb_basket CASCADE CONSTRAINTS;
DROP TABLE tb_category_product_mapping CASCADE CONSTRAINTS;
DROP TABLE tb_product CASCADE CONSTRAINTS;
DROP TABLE tb_content CASCADE CONSTRAINTS;
DROP TABLE tb_category CASCADE CONSTRAINTS;
DROP TABLE tb_user CASCADE CONSTRAINTS;

DROP SEQUENCE seq_tb_order_item;
DROP SEQUENCE seq_tb_order;
DROP SEQUENCE seq_tb_basket_item;
DROP SEQUENCE seq_tb_basket;
DROP SEQUENCE seq_tb_category_product_mapping;
DROP SEQUENCE seq_tb_product;
DROP SEQUENCE seq_tb_content;
DROP SEQUENCE seq_tb_category;
DROP SEQUENCE seq_tb_user;

CREATE SEQUENCE seq_tb_user START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_category START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_content START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_product START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_category_product_mapping START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_basket START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_basket_item START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_order START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_order_item START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE tb_user (
                         nb_user      NUMBER(9) DEFAULT seq_tb_user.NEXTVAL NOT NULL,
                         id_user      VARCHAR2(100) NOT NULL,
                         nm_user      VARCHAR2(100) NOT NULL,
                         nm_paswd     VARCHAR2(256) NOT NULL,
                         no_mobile    VARCHAR2(30) NOT NULL,
                         nm_email     VARCHAR2(100) NOT NULL,
                         st_status    VARCHAR2(4) DEFAULT 'ST03' NOT NULL,
                         cd_user_type VARCHAR2(4) DEFAULT '10' NOT NULL,
                         CONSTRAINT pk_tb_user PRIMARY KEY (nb_user),
                         CONSTRAINT uk_tb_user_id_user UNIQUE (id_user),
                         CONSTRAINT uk_tb_user_nm_email UNIQUE (nm_email),
                         CONSTRAINT ck_tb_user_st_status CHECK (st_status IN ('ST01', 'ST02', 'ST03', 'ST04')),
                         CONSTRAINT ck_tb_user_cd_user_type CHECK (cd_user_type IN ('10', '20'))
);

CREATE TABLE tb_category (
                             nb_category        NUMBER(8) DEFAULT seq_tb_category.NEXTVAL NOT NULL,
                             nb_parent_category NUMBER(8),
                             nm_category        VARCHAR2(100) NOT NULL,
                             cn_level           NUMBER(4),
                             cn_order           NUMBER(4) NOT NULL,
                             CONSTRAINT pk_tb_category PRIMARY KEY (nb_category),
                             CONSTRAINT fk_tb_category_parent FOREIGN KEY (nb_parent_category) REFERENCES tb_category (nb_category),
                             CONSTRAINT ck_tb_category_cn_level CHECK (cn_level IN (1, 2)),
                             CONSTRAINT ck_tb_category_cn_order CHECK (cn_order > 0)
);

CREATE TABLE tb_content (
                            nb_file         NUMBER(19,0) DEFAULT seq_tb_content.NEXTVAL NOT NULL,
                            nm_org_file     VARCHAR2(200),
                            nm_save_file    VARCHAR2(200),
                            nm_file_path    VARCHAR2(200),
                            nm_content_type VARCHAR2(20),
                            qt_file_size    NUMBER(19,0),
                            nm_file_ext     VARCHAR2(10) NOT NULL,
                            da_create_at    TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
                            nb_org_file     NUMBER(19,0),
                            CONSTRAINT pk_tb_content PRIMARY KEY (nb_file),
                            CONSTRAINT fk_tb_content_org_file FOREIGN KEY (nb_org_file) REFERENCES tb_content (nb_file),
                            CONSTRAINT ck_tb_content_qt_file_size CHECK (qt_file_size IS NULL OR qt_file_size >= 0)
);

CREATE INDEX idx1_tb_content ON tb_content (nb_org_file);

CREATE TABLE tb_product (
                            nb_product        NUMBER(9) DEFAULT seq_tb_product.NEXTVAL NOT NULL,
                            no_product        VARCHAR2(30) NOT NULL,
                            nm_product        VARCHAR2(200) NOT NULL,
                            nm_detail_explain VARCHAR2(4000),
                            nb_file           NUMBER(19,0),
                            dt_start_date     VARCHAR2(8),
                            dt_end_date       VARCHAR2(8),
                            qt_sale_price     NUMBER(9) NOT NULL,
                            qt_stock          NUMBER(9) DEFAULT 0,
                            CONSTRAINT pk_tb_product PRIMARY KEY (nb_product),
                            CONSTRAINT uk_tb_product_no_product UNIQUE (no_product),
                            CONSTRAINT fk_tb_product_content FOREIGN KEY (nb_file) REFERENCES tb_content (nb_file),
                            CONSTRAINT ck_tb_product_qt_sale_price CHECK (qt_sale_price >= 0),
                            CONSTRAINT ck_tb_product_qt_stock CHECK (qt_stock IS NULL OR qt_stock >= 0)
);

CREATE TABLE tb_category_product_mapping (
                                             nb_category_product_mapping NUMBER(9) DEFAULT seq_tb_category_product_mapping.NEXTVAL NOT NULL,
                                             nb_category                 NUMBER(8) NOT NULL,
                                             nb_product                  NUMBER(9) NOT NULL,
                                             cn_order                    NUMBER(4) NOT NULL,
                                             CONSTRAINT pk_tb_category_product_mapping PRIMARY KEY (nb_category_product_mapping),
                                             CONSTRAINT fk_tcpm_category FOREIGN KEY (nb_category) REFERENCES tb_category (nb_category),
                                             CONSTRAINT fk_tcpm_product FOREIGN KEY (nb_product) REFERENCES tb_product (nb_product),
                                             CONSTRAINT uk_tcpm_category_product UNIQUE (nb_category, nb_product),
                                             CONSTRAINT ck_tcpm_cn_order CHECK (cn_order > 0)
);

CREATE TABLE tb_basket (
                           nb_basket NUMBER(9) DEFAULT seq_tb_basket.NEXTVAL NOT NULL,
                           nb_user   NUMBER(9) NOT NULL,
                           CONSTRAINT pk_tb_basket PRIMARY KEY (nb_basket),
                           CONSTRAINT fk_tb_basket_user FOREIGN KEY (nb_user) REFERENCES tb_user (nb_user),
                           CONSTRAINT uk_tb_basket_user UNIQUE (nb_user)
);

CREATE TABLE tb_basket_item (
                                nb_basket_item        NUMBER(9) DEFAULT seq_tb_basket_item.NEXTVAL NOT NULL,
                                nb_basket              NUMBER(9) NOT NULL,
                                cn_basket_item_order NUMBER(9) NOT NULL,
                                nb_product            NUMBER(9) NOT NULL,
                                qt_basket_item_price NUMBER(9),
                                qt_basket_item        NUMBER(9),
                                qt_basket_item_amount NUMBER(9),
                                CONSTRAINT pk_tb_basket_item PRIMARY KEY (nb_basket_item),
                                CONSTRAINT fk_tb_basket_item_basket FOREIGN KEY (nb_basket) REFERENCES tb_basket (nb_basket),
                                CONSTRAINT fk_tb_basket_item_product FOREIGN KEY (nb_product) REFERENCES tb_product (nb_product),
                                CONSTRAINT uk_tb_basket_item_product UNIQUE (nb_basket, nb_product),
                                CONSTRAINT ck_tb_basket_item_order CHECK (cn_basket_item_order > 0),
                                CONSTRAINT ck_tb_basket_item_price CHECK (qt_basket_item_price IS NULL OR qt_basket_item_price >= 0),
                                CONSTRAINT ck_tb_basket_item_qty CHECK (qt_basket_item IS NULL OR qt_basket_item > 0),
                                CONSTRAINT ck_tb_basket_item_amount CHECK (qt_basket_item_amount IS NULL OR qt_basket_item_amount >= 0)
);

CREATE TABLE tb_order (
                          nb_order            NUMBER(9) DEFAULT seq_tb_order.NEXTVAL NOT NULL,
                          nb_user             NUMBER(9) NOT NULL,
                          qt_order_amount     NUMBER(9),
                          nm_order_person     VARCHAR2(100),
                          nm_delivery_address VARCHAR2(200),
                          da_order            DATE DEFAULT SYSDATE,
                          st_order            VARCHAR2(4) DEFAULT 'OR01',
                          CONSTRAINT pk_tb_order PRIMARY KEY (nb_order),
                          CONSTRAINT fk_tb_order_user FOREIGN KEY (nb_user) REFERENCES tb_user (nb_user),
                          CONSTRAINT ck_tb_order_amount CHECK (qt_order_amount IS NULL OR qt_order_amount >= 0),
                          CONSTRAINT ck_tb_order_st_order CHECK (st_order IN ('OR01', 'OR02', 'OR03', 'OR04', 'OR05'))
);

CREATE TABLE tb_order_item (
                               nb_order_item NUMBER(9) DEFAULT seq_tb_order_item.NEXTVAL NOT NULL,
                               nb_order      NUMBER(9) NOT NULL,
                               cn_order_item NUMBER(5) NOT NULL,
                               nb_product    NUMBER(9) NOT NULL,
                               nb_user       NUMBER(9) NOT NULL,
                               qt_unit_price NUMBER(9) NOT NULL,
                               qt_order_item NUMBER(9) NOT NULL,
                               CONSTRAINT pk_tb_order_item PRIMARY KEY (nb_order_item),
                               CONSTRAINT fk_tb_order_item_order FOREIGN KEY (nb_order) REFERENCES tb_order (nb_order),
                               CONSTRAINT fk_tb_order_item_product FOREIGN KEY (nb_product) REFERENCES tb_product (nb_product),
                               CONSTRAINT fk_tb_order_item_user FOREIGN KEY (nb_user) REFERENCES tb_user (nb_user),
                               CONSTRAINT ck_tb_order_item_order CHECK (cn_order_item > 0),
                               CONSTRAINT ck_tb_order_item_price CHECK (qt_unit_price >= 0),
                               CONSTRAINT ck_tb_order_item_qty CHECK (qt_order_item > 0)
);

SET DEFINE OFF;

BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_order_item CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_order CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_basket_item CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_basket CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_category_product_mapping CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_product CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_content CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_category CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE tb_user CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/

BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_order_item'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_order'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_basket_item'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_basket'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_category_product_mapping'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_product'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_content'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_category'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE seq_tb_user'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/

CREATE SEQUENCE seq_tb_user START WITH 100 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_category START WITH 100 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_content START WITH 100 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_product START WITH 100 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_category_product_mapping START WITH 100 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_basket START WITH 100 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_basket_item START WITH 100 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_order START WITH 100 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_tb_order_item START WITH 100 INCREMENT BY 1 NOCACHE;

CREATE TABLE tb_user (
                         nb_user      NUMBER(9) DEFAULT seq_tb_user.NEXTVAL NOT NULL,
                         id_user      VARCHAR2(100) NOT NULL,
                         nm_user      VARCHAR2(100) NOT NULL,
                         nm_paswd     VARCHAR2(256) NOT NULL,
                         no_mobile    VARCHAR2(30) NOT NULL,
                         nm_email     VARCHAR2(100) NOT NULL,
                         st_status    VARCHAR2(4) DEFAULT 'ST03' NOT NULL,
                         cd_user_type VARCHAR2(4) DEFAULT '10' NOT NULL,
                         CONSTRAINT pk_tb_user PRIMARY KEY (nb_user),
                         CONSTRAINT uk_tb_user_id_user UNIQUE (id_user),
                         CONSTRAINT ck_tb_user_st_status CHECK (st_status IN ('ST01', 'ST02', 'ST03', 'ST04')),
                         CONSTRAINT ck_tb_user_cd_user_type CHECK (cd_user_type IN ('10', '20'))
);

CREATE TABLE tb_category (
                             nb_category        NUMBER(8) DEFAULT seq_tb_category.NEXTVAL NOT NULL,
                             nb_parent_category NUMBER(8),
                             nm_category        VARCHAR2(100) NOT NULL,
                             cn_level           NUMBER(4),
                             cn_order           NUMBER(4) NOT NULL,
                             CONSTRAINT pk_tb_category PRIMARY KEY (nb_category),
                             CONSTRAINT fk_tb_category_parent FOREIGN KEY (nb_parent_category) REFERENCES tb_category (nb_category),
                             CONSTRAINT ck_tb_category_cn_level CHECK (cn_level IN (1, 2)),
                             CONSTRAINT ck_tb_category_cn_order CHECK (cn_order > 0)
);

CREATE TABLE tb_content (
                            nb_file         NUMBER(19,0) DEFAULT seq_tb_content.NEXTVAL NOT NULL,
                            nm_org_file     VARCHAR2(200),
                            nm_save_file    VARCHAR2(200),
                            nm_file_path    VARCHAR2(200),
                            nm_content_type VARCHAR2(20),
                            qt_file_size    NUMBER(19,0),
                            nm_file_ext     VARCHAR2(10) NOT NULL,
                            da_create_at    TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT NULL,
                            nb_org_file     NUMBER(19,0),
                            CONSTRAINT pk_tb_content PRIMARY KEY (nb_file),
                            CONSTRAINT fk_tb_content_org_file FOREIGN KEY (nb_org_file) REFERENCES tb_content (nb_file),
                            CONSTRAINT ck_tb_content_qt_file_size CHECK (qt_file_size IS NULL OR qt_file_size >= 0)
);

CREATE INDEX idx1_tb_content ON tb_content (nb_org_file);

CREATE TABLE tb_product (
                            nb_product        NUMBER(9) DEFAULT seq_tb_product.NEXTVAL NOT NULL,
                            no_product        VARCHAR2(30) NOT NULL,
                            nm_product        VARCHAR2(200) NOT NULL,
                            nm_detail_explain VARCHAR2(4000),
                            nb_file           NUMBER(19,0),
                            dt_start_date     VARCHAR2(8),
                            dt_end_date       VARCHAR2(8),
                            qt_sale_price     NUMBER(9) NOT NULL,
                            qt_stock          NUMBER(9) DEFAULT 0,
                            CONSTRAINT pk_tb_product PRIMARY KEY (nb_product),
                            CONSTRAINT uk_tb_product_no_product UNIQUE (no_product),
                            CONSTRAINT fk_tb_product_content FOREIGN KEY (nb_file) REFERENCES tb_content (nb_file),
                            CONSTRAINT ck_tb_product_qt_sale_price CHECK (qt_sale_price >= 0),
                            CONSTRAINT ck_tb_product_qt_stock CHECK (qt_stock IS NULL OR qt_stock >= 0)
);

CREATE TABLE tb_category_product_mapping (
                                             nb_category_product_mapping NUMBER(9) DEFAULT seq_tb_category_product_mapping.NEXTVAL NOT NULL,
                                             nb_category                 NUMBER(8) NOT NULL,
                                             nb_product                  NUMBER(9) NOT NULL,
                                             cn_order                    NUMBER(4) NOT NULL,
                                             CONSTRAINT pk_tb_category_product_mapping PRIMARY KEY (nb_category_product_mapping),
                                             CONSTRAINT fk_tcpm_category FOREIGN KEY (nb_category) REFERENCES tb_category (nb_category),
                                             CONSTRAINT fk_tcpm_product FOREIGN KEY (nb_product) REFERENCES tb_product (nb_product),
                                             CONSTRAINT uk_tcpm_category_product UNIQUE (nb_category, nb_product),
                                             CONSTRAINT ck_tcpm_cn_order CHECK (cn_order > 0)
);

CREATE TABLE tb_basket (
                           nb_basket NUMBER(9) DEFAULT seq_tb_basket.NEXTVAL NOT NULL,
                           nb_user   NUMBER(9) NOT NULL,
                           CONSTRAINT pk_tb_basket PRIMARY KEY (nb_basket),
                           CONSTRAINT fk_tb_basket_user FOREIGN KEY (nb_user) REFERENCES tb_user (nb_user),
                           CONSTRAINT uk_tb_basket_user UNIQUE (nb_user)
);

CREATE TABLE tb_basket_item (
                                nb_basket_item        NUMBER(9) DEFAULT seq_tb_basket_item.NEXTVAL NOT NULL,
                                nb_basket              NUMBER(9) NOT NULL,
                                cn_basket_item_order  NUMBER(9) NOT NULL,
                                nb_product            NUMBER(9) NOT NULL,
                                qt_basket_item_price  NUMBER(9),
                                qt_basket_item        NUMBER(9),
                                qt_basket_item_amount NUMBER(9),
                                CONSTRAINT pk_tb_basket_item PRIMARY KEY (nb_basket_item),
                                CONSTRAINT fk_tb_basket_item_basket FOREIGN KEY (nb_basket) REFERENCES tb_basket (nb_basket),
                                CONSTRAINT fk_tb_basket_item_product FOREIGN KEY (nb_product) REFERENCES tb_product (nb_product),
                                CONSTRAINT uk_tb_basket_item_product UNIQUE (nb_basket, nb_product),
                                CONSTRAINT ck_tb_basket_item_order CHECK (cn_basket_item_order > 0),
                                CONSTRAINT ck_tb_basket_item_price CHECK (qt_basket_item_price IS NULL OR qt_basket_item_price >= 0),
                                CONSTRAINT ck_tb_basket_item_qty CHECK (qt_basket_item IS NULL OR qt_basket_item > 0),
                                CONSTRAINT ck_tb_basket_item_amount CHECK (qt_basket_item_amount IS NULL OR qt_basket_item_amount >= 0)
);

CREATE TABLE tb_order (
                          nb_order            NUMBER(9) DEFAULT seq_tb_order.NEXTVAL NOT NULL,
                          nb_user             NUMBER(9) NOT NULL,
                          qt_order_amount     NUMBER(9),
                          nm_order_person     VARCHAR2(100),
                          nm_delivery_address VARCHAR2(200),
                          da_order            DATE DEFAULT SYSDATE,
                          st_order            VARCHAR2(4) DEFAULT 'OR01',
                          CONSTRAINT pk_tb_order PRIMARY KEY (nb_order),
                          CONSTRAINT fk_tb_order_user FOREIGN KEY (nb_user) REFERENCES tb_user (nb_user),
                          CONSTRAINT ck_tb_order_amount CHECK (qt_order_amount IS NULL OR qt_order_amount >= 0),
                          CONSTRAINT ck_tb_order_st_order CHECK (st_order IN ('OR01', 'OR02', 'OR03', 'OR04', 'OR05'))
);

CREATE TABLE tb_order_item (
                               nb_order_item NUMBER(9) DEFAULT seq_tb_order_item.NEXTVAL NOT NULL,
                               nb_order      NUMBER(9) NOT NULL,
                               cn_order_item NUMBER(5) NOT NULL,
                               nb_product    NUMBER(9) NOT NULL,
                               nb_user       NUMBER(9) NOT NULL,
                               qt_unit_price NUMBER(9) NOT NULL,
                               qt_order_item NUMBER(9) NOT NULL,
                               CONSTRAINT pk_tb_order_item PRIMARY KEY (nb_order_item),
                               CONSTRAINT fk_tb_order_item_order FOREIGN KEY (nb_order) REFERENCES tb_order (nb_order),
                               CONSTRAINT fk_tb_order_item_product FOREIGN KEY (nb_product) REFERENCES tb_product (nb_product),
                               CONSTRAINT fk_tb_order_item_user FOREIGN KEY (nb_user) REFERENCES tb_user (nb_user),
                               CONSTRAINT ck_tb_order_item_order CHECK (cn_order_item > 0),
                               CONSTRAINT ck_tb_order_item_price CHECK (qt_unit_price >= 0),
                               CONSTRAINT ck_tb_order_item_qty CHECK (qt_order_item > 0)
);

-- 샘플 계정 (개인정보 제거 · 비밀번호 해시는 placeholder입니다. 실제 사용 시 본인 BCrypt 해시로 교체하세요)
Insert into TB_USER (NB_USER,ID_USER,NM_USER,NM_PASWD,NO_MOBILE,NM_EMAIL,ST_STATUS,CD_USER_TYPE) values (1,'admin','관리자','$2a$10$REPLACE.WITH.YOUR.OWN.BCRYPT.HASH.PLACEHOLDER.000000','010-0000-0000','admin@example.com','ST01','20');
Insert into TB_USER (NB_USER,ID_USER,NM_USER,NM_PASWD,NO_MOBILE,NM_EMAIL,ST_STATUS,CD_USER_TYPE) values (2,'user02','회원2','$2a$10$REPLACE.WITH.YOUR.OWN.BCRYPT.HASH.PLACEHOLDER.000000','010-0000-0000','user02@example.com','ST02','10');
Insert into TB_USER (NB_USER,ID_USER,NM_USER,NM_PASWD,NO_MOBILE,NM_EMAIL,ST_STATUS,CD_USER_TYPE) values (3,'user03','회원3','$2a$10$REPLACE.WITH.YOUR.OWN.BCRYPT.HASH.PLACEHOLDER.000000','010-0000-0000','user03@example.com','ST01','10');
Insert into TB_USER (NB_USER,ID_USER,NM_USER,NM_PASWD,NO_MOBILE,NM_EMAIL,ST_STATUS,CD_USER_TYPE) values (4,'user04','회원4','$2a$10$REPLACE.WITH.YOUR.OWN.BCRYPT.HASH.PLACEHOLDER.000000','010-0000-0000','user04@example.com','ST04','10');

Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (15,null,'Black & White',1,4);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (5,null,'Landscape',1,1);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (6,null,'Architecture',1,2);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (7,null,'Portrait',1,3);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (8,5,'자연 풍경',2,1);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (9,5,'계절 사진',2,2);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (11,5,'일몰/일출',2,4);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (12,6,'현대 건축',2,1);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (13,6,'전통 건축',2,2);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (14,7,'감성 인물',2,1);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (16,15,'흑백 풍경',2,1);
Insert into TB_CATEGORY (NB_CATEGORY,NB_PARENT_CATEGORY,NM_CATEGORY,CN_LEVEL,CN_ORDER) values (17,5,'도시 풍경',2,3);

Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (2,'스크린샷 2026-05-13 183743.png','bf03bb1b-6446-43ee-9ea7-d59ebb3bd4aa.png','uploads/products','image/png',1131241,'png',to_timestamp('26/05/16 01:24:47.801336000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (8,'일몰.webp','d5231a5e-8f5e-4f5a-bfa8-708045e88207.webp','uploads/products','image/webp',17412120,'webp',to_timestamp('26/05/16 02:33:09.090924000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (9,'계절-사진.webp','cc32f9f8-814e-455a-88e0-ae0fffadf640.webp','uploads/products','image/webp',4345374,'webp',to_timestamp('26/05/16 02:34:06.210800000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (10,'전통사진.webp','c7f79c2a-e0e0-49ec-92cb-b147f3c706ab.webp','uploads/products','image/webp',3424588,'webp',to_timestamp('26/05/16 02:53:26.395189000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (11,'감성_사람들_사진.webp','2239acc8-d45f-45f4-b8dd-e47277946182.webp','uploads/products','image/webp',787594,'webp',to_timestamp('26/05/16 02:54:13.062998000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (12,'흑백_건축사진.webp','b3c4b90c-b739-407e-a95b-a1332607f499.webp','uploads/products','image/webp',1439418,'webp',to_timestamp('26/05/16 02:55:12.080883000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,nm_org_file,nm_save_file,nm_file_path,nm_content_type,qt_file_size,nm_file_ext,da_create_at,nb_org_file) values (13,'현대예술건물사진.webp','2d142f6b-40af-4ab0-a0d3-15521dab6d2e.webp','uploads/products','image/webp',766470,'webp',to_timestamp('26/05/16 02:55:45.422096000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (14,'도시.webp','cbfcba2f-19b2-427f-a51c-7e5a016c3fe1.webp','uploads/products','image/webp',2160938,'webp',to_timestamp('26/05/16 02:56:26.728957000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (1,'하.jpg','44d1a362-c4ce-4ae4-88a9-6aaa6b2281c3.jpg','uploads/products','image/jpeg',134206,'jpg',to_timestamp('26/05/15 10:05:23.606493000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (3,'자연-풍경.webp','13888cf7-1b29-4d2c-ba94-fd1c50d144bd.webp','uploads/products','image/webp',3444558,'webp',to_timestamp('26/05/16 02:21:02.019450000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (4,'자연-풍경-_2_.webp','6ff8eb8f-1690-4073-a892-3c07f169c48b.webp','uploads/products','image/webp',5770612,'webp',to_timestamp('26/05/16 02:22:55.404986000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (5,'자연-풍경-_2_.webp','dc95da38-2775-486e-ae74-b2fb581e90fd.webp','uploads/products','image/webp',5770612,'webp',to_timestamp('26/05/16 02:23:09.821999000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (6,'자연-풍경-_2_.webp','b45e4bd8-2d40-45ec-8bf4-963f2230ba8d.webp','uploads/products','image/webp',5770612,'webp',to_timestamp('26/05/16 02:24:08.098337000','RR/MM/DD HH24:MI:SSXFF'),null);
Insert into TB_CONTENT (NB_FILE,NM_ORG_FILE,NM_SAVE_FILE,NM_FILE_PATH,NM_CONTENT_TYPE,QT_FILE_SIZE,NM_FILE_EXT,DA_CREATE_AT,NB_ORG_FILE) values (7,'자연-풍경-_2_.webp','30225b34-b18b-4cdc-b241-be6bde904fa8.webp','uploads/products','image/webp',5770612,'webp',to_timestamp('26/05/16 02:28:34.513504000','RR/MM/DD HH24:MI:SSXFF'),null);

Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (5,'PRD-1778866389097','멋진 일몰 ','멋진 일몰 ',8,'20260516',null,49999,8);
Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (6,'PRD-1778866446218','분홍분홍 풍경','분홍분홍 풍경',9,'20260516',null,99999,2);
Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (7,'PRD-1778867606407','중국 건물','중국 건물',10,'20260516',null,17000,0);
Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (8,'PRD-1778867653069','흑백 사람들','흑백 사람들',11,'20260516',null,5000,30);
Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (9,'PRD-1778867712086','흑백 건축사진','흑백 건축사진',12,'20260516',null,79999,13);
Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (10,'PRD-1778867745429','현대 예술 건물','현대 예술 건물',13,'20260516',null,35000,15);
Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (11,'PRD-1778867786738','아름다운 도시','아름다운 도시',14,'20260516',null,78000,2);
Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (3,'PRD-1778865662037','아름다운 경관','케나다에서 찍은 사진\nCanon, EOS 5D Mark',3,'20260516',null,33000,5);
Insert into TB_PRODUCT (NB_PRODUCT,NO_PRODUCT,NM_PRODUCT,NM_DETAIL_EXPLAIN,NB_FILE,DT_START_DATE,DT_END_DATE,QT_SALE_PRICE,QT_STOCK) values (4,'PRD-1778865775412','아름다운 바다','아름다운 바다',7,'20260516',null,15000,6);

Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (5,11,5,1);
Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (6,9,6,1);
Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (7,13,7,1);
Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (8,14,8,1);
Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (9,12,9,1);
Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (10,12,10,1);
Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (11,17,11,1);
Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (3,8,3,1);
Insert into TB_CATEGORY_PRODUCT_MAPPING (NB_CATEGORY_PRODUCT_MAPPING,NB_CATEGORY,NB_PRODUCT,CN_ORDER) values (4,8,4,1);

Insert into TB_BASKET (NB_BASKET,NB_USER) values (1,1);
Insert into TB_BASKET (NB_BASKET,NB_USER) values (2,3);
Insert into TB_BASKET (NB_BASKET,NB_USER) values (3,4);

Insert into TB_ORDER (NB_ORDER,NB_USER,QT_ORDER_AMOUNT,NM_ORDER_PERSON,NM_DELIVERY_ADDRESS,DA_ORDER,ST_ORDER) values (2,1,78000,'관리자','기본 배송지',to_date('26/05/16','RR/MM/DD'),'OR01');
Insert into TB_ORDER (NB_ORDER,NB_USER,QT_ORDER_AMOUNT,NM_ORDER_PERSON,NM_DELIVERY_ADDRESS,DA_ORDER,ST_ORDER) values (3,3,85000,'회원3','기본 배송지',to_date('26/05/16','RR/MM/DD'),'OR01');
Insert into TB_ORDER (NB_ORDER,NB_USER,QT_ORDER_AMOUNT,NM_ORDER_PERSON,NM_DELIVERY_ADDRESS,DA_ORDER,ST_ORDER) values (4,3,5000,'회원3','기본 배송지',to_date('26/05/16','RR/MM/DD'),'OR01');
Insert into TB_ORDER (NB_ORDER,NB_USER,QT_ORDER_AMOUNT,NM_ORDER_PERSON,NM_DELIVERY_ADDRESS,DA_ORDER,ST_ORDER) values (5,4,55000,'회원4','기본 배송지',to_date('26/05/16','RR/MM/DD'),'OR01');
Insert into TB_ORDER (NB_ORDER,NB_USER,QT_ORDER_AMOUNT,NM_ORDER_PERSON,NM_DELIVERY_ADDRESS,DA_ORDER,ST_ORDER) values (1,1,907000,'관리자','기본 배송지',to_date('26/05/16','RR/MM/DD'),'OR01');

Insert into TB_ORDER_ITEM (NB_ORDER_ITEM,NB_ORDER,CN_ORDER_ITEM,NB_PRODUCT,NB_USER,QT_UNIT_PRICE,QT_ORDER_ITEM) values (3,2,1,11,1,78000,1);
Insert into TB_ORDER_ITEM (NB_ORDER_ITEM,NB_ORDER,CN_ORDER_ITEM,NB_PRODUCT,NB_USER,QT_UNIT_PRICE,QT_ORDER_ITEM) values (4,3,1,7,3,17000,5);
Insert into TB_ORDER_ITEM (NB_ORDER_ITEM,NB_ORDER,CN_ORDER_ITEM,NB_PRODUCT,NB_USER,QT_UNIT_PRICE,QT_ORDER_ITEM) values (5,4,1,8,3,5000,1);
Insert into TB_ORDER_ITEM (NB_ORDER_ITEM,NB_ORDER,CN_ORDER_ITEM,NB_PRODUCT,NB_USER,QT_UNIT_PRICE,QT_ORDER_ITEM) values (6,5,1,4,4,15000,1);
Insert into TB_ORDER_ITEM (NB_ORDER_ITEM,NB_ORDER,CN_ORDER_ITEM,NB_PRODUCT,NB_USER,QT_UNIT_PRICE,QT_ORDER_ITEM) values (7,5,2,10,4,35000,1);
Insert into TB_ORDER_ITEM (NB_ORDER_ITEM,NB_ORDER,CN_ORDER_ITEM,NB_PRODUCT,NB_USER,QT_UNIT_PRICE,QT_ORDER_ITEM) values (8,5,3,8,4,5000,1);
Insert into TB_ORDER_ITEM (NB_ORDER_ITEM,NB_ORDER,CN_ORDER_ITEM,NB_PRODUCT,NB_USER,QT_UNIT_PRICE,QT_ORDER_ITEM) values (1,1,1,10,1,35000,17);
Insert into TB_ORDER_ITEM (NB_ORDER_ITEM,NB_ORDER,CN_ORDER_ITEM,NB_PRODUCT,NB_USER,QT_UNIT_PRICE,QT_ORDER_ITEM) values (2,1,2,11,1,78000,4);

COMMIT;

select * from tb_order;