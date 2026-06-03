/* BasketMapper - 장바구니 품목 조회 */
SELECT
    bi.nb_basket_item AS nbBasketItem,
    p.nb_product AS nbProduct,
    p.nm_product AS nmProduct,
    CASE
        WHEN c.nm_save_file IS NOT NULL THEN '/files/' || c.nm_save_file
        ELSE NULL
        END AS imagePath,
    bi.qt_basket_item_price AS unitPrice,
    bi.qt_basket_item AS quantity,
    bi.qt_basket_item_amount AS amount
FROM tb_basket_item bi
         JOIN tb_product p
              ON bi.nb_product = p.nb_product
         LEFT JOIN tb_content c
                   ON p.nb_file = c.nb_file
WHERE bi.nb_basket = :nbBasket
ORDER BY bi.cn_basket_item_order ASC;