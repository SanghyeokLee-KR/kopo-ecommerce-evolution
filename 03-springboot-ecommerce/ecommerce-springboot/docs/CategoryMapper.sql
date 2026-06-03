/* CategoryMapper - 카테고리 목록 조회 */
SELECT
    c.nb_category AS nbCategory,
    c.nb_parent_category AS nbParentCategory,
    c.nm_category AS nmCategory,
    c.cn_level AS cnLevel,
    c.cn_order AS cnOrder
FROM tb_category c
ORDER BY c.cn_level ASC, c.cn_order ASC;