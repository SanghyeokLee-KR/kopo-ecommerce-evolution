/* ProductMapper - 관리자 상품 전체 조회 */
SELECT
    p.nb_product AS nbProduct,
    p.nm_product AS nmProduct,
    p.qt_sale_price AS qtPrice,
    p.qt_stock AS qtStock,
    p.dt_start_date AS dtSellStart,
    CASE
        WHEN p.qt_stock IS NOT NULL AND p.qt_stock <= 0 THEN 'ST02'
        ELSE 'ST01'
        END AS stProduct,
    CASE
        WHEN c.nm_save_file IS NOT NULL THEN '/files/' || c.nm_save_file
        ELSE NULL
        END AS imagePath
FROM tb_product p
         LEFT JOIN tb_content c
                   ON p.nb_file = c.nb_file
ORDER BY p.nb_product DESC;


/* ProductMapper - 관리자 상품 페이징 조회 */
SELECT
    p.nb_product AS nbProduct,
    p.nm_product AS nmProduct,
    p.qt_sale_price AS qtPrice,
    p.qt_stock AS qtStock,
    p.dt_start_date AS dtSellStart,
    CASE
        WHEN p.qt_stock IS NOT NULL AND p.qt_stock <= 0 THEN 'ST02'
        ELSE 'ST01'
        END AS stProduct,
    CASE
        WHEN c.nm_save_file IS NOT NULL THEN '/files/' || c.nm_save_file
        ELSE NULL
        END AS imagePath
FROM tb_product p
         LEFT JOIN tb_content c
                   ON p.nb_file = c.nb_file
ORDER BY p.nb_product DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;


/* ProductMapper - 관리자 상품 총 개수 */
SELECT COUNT(*)
FROM tb_product;


/* ProductMapper - 사용자 상품 목록 조회 */
SELECT
    p.nb_product AS nbProduct,
    p.nm_product AS nmProduct,
    p.qt_sale_price AS qtPrice,
    p.qt_stock AS qtStock,
    p.dt_start_date AS dtSellStart,
    CASE
        WHEN p.qt_stock IS NOT NULL AND p.qt_stock <= 0 THEN 'ST02'
        ELSE 'ST01'
        END AS stProduct,
    CASE
        WHEN c.nm_save_file IS NOT NULL THEN '/files/' || c.nm_save_file
        ELSE NULL
        END AS imagePath
FROM tb_product p
         LEFT JOIN tb_content c
                   ON p.nb_file = c.nb_file
WHERE (
          :keyword IS NULL
              OR p.nm_product LIKE '%' || :keyword || '%'
              OR p.nm_detail_explain LIKE '%' || :keyword || '%'
          )
ORDER BY p.nb_product DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;


/* ProductMapper - 사용자 상품 총 개수 */
SELECT COUNT(*)
FROM tb_product p
WHERE (
          :keyword IS NULL
              OR p.nm_product LIKE '%' || :keyword || '%'
              OR p.nm_detail_explain LIKE '%' || :keyword || '%'
          );


/* ProductMapper - 카테고리별 상품 목록 조회 */
SELECT DISTINCT
    p.nb_product AS nbProduct,
    p.nm_product AS nmProduct,
    p.qt_sale_price AS qtPrice,
    p.qt_stock AS qtStock,
    p.dt_start_date AS dtSellStart,
    CASE
        WHEN p.qt_stock IS NOT NULL AND p.qt_stock <= 0 THEN 'ST02'
        ELSE 'ST01'
        END AS stProduct,
    CASE
        WHEN c.nm_save_file IS NOT NULL THEN '/files/' || c.nm_save_file
        ELSE NULL
        END AS imagePath
FROM tb_product p
         JOIN tb_category_product_mapping m
              ON p.nb_product = m.nb_product
         JOIN tb_category cat
              ON m.nb_category = cat.nb_category
         LEFT JOIN tb_category parent
                   ON cat.nb_parent_category = parent.nb_category
         LEFT JOIN tb_content c
                   ON p.nb_file = c.nb_file
WHERE (cat.nb_category = :nbCategory OR parent.nb_category = :nbCategory)
  AND (
    :keyword IS NULL
        OR p.nm_product LIKE '%' || :keyword || '%'
        OR p.nm_detail_explain LIKE '%' || :keyword || '%'
    )
ORDER BY p.nb_product DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;


/* ProductMapper - 카테고리별 상품 총 개수 */
SELECT COUNT(DISTINCT p.nb_product)
FROM tb_product p
         JOIN tb_category_product_mapping m
              ON p.nb_product = m.nb_product
         JOIN tb_category cat
              ON m.nb_category = cat.nb_category
         LEFT JOIN tb_category parent
                   ON cat.nb_parent_category = parent.nb_category
WHERE (cat.nb_category = :nbCategory OR parent.nb_category = :nbCategory)
  AND (
    :keyword IS NULL
        OR p.nm_product LIKE '%' || :keyword || '%'
        OR p.nm_detail_explain LIKE '%' || :keyword || '%'
    );