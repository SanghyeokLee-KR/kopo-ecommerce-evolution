package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.ProductCategoryMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryMappingRepository {

    public List<ProductCategoryMapping> findAll(Connection con) throws SQLException {
        List<ProductCategoryMapping> mappings = new ArrayList<>();

        String sql = """
                SELECT MAPPING_NO, PRODUCT_NO, CATEGORY_NO, SORT_ORDER
                FROM PRODUCT_CATEGORY_MAPPING
                ORDER BY SORT_ORDER, MAPPING_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                mappings.add(mapToMapping(rs));
            }
        }

        return mappings;
    }

    // 상품명, 카테고리명 포함 전체 조회
    public List<ProductCategoryMapping> findAllWithNames(Connection con) throws SQLException {
        List<ProductCategoryMapping> mappings = new ArrayList<>();

        String sql = """
                SELECT PCM.MAPPING_NO,
                       PCM.PRODUCT_NO,
                       P.PRODUCT_NAME,
                       PCM.CATEGORY_NO,
                       C.CATEGORY_NAME,
                       PCM.SORT_ORDER
                FROM PRODUCT_CATEGORY_MAPPING PCM
                JOIN PRODUCTS P
                  ON PCM.PRODUCT_NO = P.PRODUCT_NO
                JOIN CATEGORIES C
                  ON PCM.CATEGORY_NO = C.CATEGORY_NO
                ORDER BY PCM.SORT_ORDER, PCM.MAPPING_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                mappings.add(
                        ProductCategoryMapping.builder()
                                .mappingNo(rs.getInt("MAPPING_NO"))
                                .productNo(rs.getInt("PRODUCT_NO"))
                                .productName(rs.getString("PRODUCT_NAME"))
                                .categoryNo(rs.getInt("CATEGORY_NO"))
                                .categoryName(rs.getString("CATEGORY_NAME"))
                                .sortOrder(rs.getInt("SORT_ORDER"))
                                .build()
                );
            }
        }

        return mappings;
    }

    public ProductCategoryMapping findByMappingNo(Connection con, int mappingNo) throws SQLException {
        String sql = """
                SELECT MAPPING_NO, PRODUCT_NO, CATEGORY_NO, SORT_ORDER
                FROM PRODUCT_CATEGORY_MAPPING
                WHERE MAPPING_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, mappingNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToMapping(rs);
                }
            }
        }

        return null;
    }

    public List<ProductCategoryMapping> findByProductNo(Connection con, int productNo) throws SQLException {
        List<ProductCategoryMapping> mappings = new ArrayList<>();

        String sql = """
                SELECT MAPPING_NO, PRODUCT_NO, CATEGORY_NO, SORT_ORDER
                FROM PRODUCT_CATEGORY_MAPPING
                WHERE PRODUCT_NO = ?
                ORDER BY SORT_ORDER, MAPPING_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, productNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mappings.add(mapToMapping(rs));
                }
            }
        }

        return mappings;
    }

    public List<ProductCategoryMapping> findByCategoryNo(Connection con, int categoryNo) throws SQLException {
        List<ProductCategoryMapping> mappings = new ArrayList<>();

        String sql = """
                SELECT MAPPING_NO, PRODUCT_NO, CATEGORY_NO, SORT_ORDER
                FROM PRODUCT_CATEGORY_MAPPING
                WHERE CATEGORY_NO = ?
                ORDER BY SORT_ORDER, MAPPING_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, categoryNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mappings.add(mapToMapping(rs));
                }
            }
        }

        return mappings;
    }

    public boolean existsMapping(Connection con, int productNo, int categoryNo) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM PRODUCT_CATEGORY_MAPPING
                WHERE PRODUCT_NO = ? AND CATEGORY_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, productNo);
            pstmt.setInt(2, categoryNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public void save(Connection con, ProductCategoryMapping mapping) throws SQLException {
        String sql = """
                INSERT INTO PRODUCT_CATEGORY_MAPPING (
                    MAPPING_NO, PRODUCT_NO, CATEGORY_NO, SORT_ORDER
                ) VALUES (
                    SEQ_PRODUCT_CATEGORY_MAPPING.NEXTVAL, ?, ?, ?
                )
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, mapping.getProductNo());
            pstmt.setInt(2, mapping.getCategoryNo());
            pstmt.setInt(3, mapping.getSortOrder());

            pstmt.executeUpdate();
        }
    }

    public void updateSortOrder(Connection con, int mappingNo, int sortOrder) throws SQLException {
        String sql = """
                UPDATE PRODUCT_CATEGORY_MAPPING
                SET SORT_ORDER = ?
                WHERE MAPPING_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, sortOrder);
            pstmt.setInt(2, mappingNo);

            pstmt.executeUpdate();
        }
    }

    public void deleteByMappingNo(Connection con, int mappingNo) throws SQLException {
        String sql = "DELETE FROM PRODUCT_CATEGORY_MAPPING WHERE MAPPING_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, mappingNo);
            pstmt.executeUpdate();
        }
    }

    public void deleteByProductNo(Connection con, int productNo) throws SQLException {
        String sql = "DELETE FROM PRODUCT_CATEGORY_MAPPING WHERE PRODUCT_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, productNo);
            pstmt.executeUpdate();
        }
    }

    private ProductCategoryMapping mapToMapping(ResultSet rs) throws SQLException {
        return ProductCategoryMapping.builder()
                .mappingNo(rs.getInt("MAPPING_NO"))
                .productNo(rs.getInt("PRODUCT_NO"))
                .categoryNo(rs.getInt("CATEGORY_NO"))
                .sortOrder(rs.getInt("SORT_ORDER"))
                .build();
    }
}