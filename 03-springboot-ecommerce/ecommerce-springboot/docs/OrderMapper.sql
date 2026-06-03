/* OrderMapper - 사용자 주문 목록 조회 */
SELECT
    o.nb_order AS nbOrder,
    TO_CHAR(o.da_order, 'YYYY-MM-DD HH24:MI') AS daOrder,
    o.st_order AS stOrder,
    o.qt_order_amount AS orderAmount
FROM tb_order o
WHERE o.nb_user = :nbUser
ORDER BY o.nb_order DESC;


/* OrderMapper - 사용자 주문 상세 기본 정보 조회 */
SELECT
    o.nb_order AS nbOrder,
    o.nm_order_person AS nmOrderPerson,
    o.nm_delivery_address AS nmDeliveryAddress,
    TO_CHAR(o.da_order, 'YYYY-MM-DD HH24:MI') AS daOrder,
    o.st_order AS stOrder,
    o.qt_order_amount AS orderAmount
FROM tb_order o
WHERE o.nb_order = :nbOrder
  AND o.nb_user = :nbUser;


/* OrderMapper - 사용자 주문 상세 품목 조회 */
SELECT
    p.nb_product AS nbProduct,
    p.nm_product AS nmProduct,
    CASE
        WHEN c.nm_save_file IS NOT NULL THEN '/files/' || c.nm_save_file
        ELSE NULL
        END AS imagePath,
    oi.qt_unit_price AS unitPrice,
    oi.qt_order_item AS quantity,
    oi.qt_unit_price * oi.qt_order_item AS amount
FROM tb_order_item oi
         JOIN tb_product p
              ON oi.nb_product = p.nb_product
         LEFT JOIN tb_content c
                   ON p.nb_file = c.nb_file
WHERE oi.nb_order = :nbOrder
ORDER BY oi.cn_order_item ASC;


/* OrderMapper - 관리자 주문 목록 페이징 조회 */
SELECT
    o.nb_order AS nbOrder,
    u.nb_user AS nbUser,
    u.nm_user AS nmUser,
    o.nm_order_person AS nmOrderPerson,
    TO_CHAR(o.da_order, 'YYYY-MM-DD HH24:MI') AS daOrder,
    o.st_order AS stOrder,
    o.qt_order_amount AS orderAmount
FROM tb_order o
         JOIN tb_user u
              ON o.nb_user = u.nb_user
ORDER BY o.nb_order DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;


/* OrderMapper - 관리자 주문 총 개수 */
SELECT COUNT(*)
FROM tb_order;


/* OrderMapper - 관리자 주문 상세 기본 정보 조회 */
SELECT
    o.nb_order AS nbOrder,
    u.nb_user AS nbUser,
    u.nm_user AS nmUser,
    o.nm_order_person AS nmOrderPerson,
    o.nm_delivery_address AS nmDeliveryAddress,
    TO_CHAR(o.da_order, 'YYYY-MM-DD HH24:MI') AS daOrder,
    o.st_order AS stOrder,
    o.qt_order_amount AS orderAmount
FROM tb_order o
         JOIN tb_user u
              ON o.nb_user = u.nb_user
WHERE o.nb_order = :nbOrder;


/* OrderMapper - 관리자 주문 상세 품목 조회 */
SELECT
    p.nb_product AS nbProduct,
    p.nm_product AS nmProduct,
    CASE
        WHEN c.nm_save_file IS NOT NULL THEN '/files/' || c.nm_save_file
        ELSE NULL
        END AS imagePath,
    oi.qt_unit_price AS unitPrice,
    oi.qt_order_item AS quantity,
    oi.qt_unit_price * oi.qt_order_item AS amount
FROM tb_order_item oi
         JOIN tb_product p
              ON oi.nb_product = p.nb_product
         LEFT JOIN tb_content c
                   ON p.nb_file = c.nb_file
WHERE oi.nb_order = :nbOrder
ORDER BY oi.cn_order_item ASC;