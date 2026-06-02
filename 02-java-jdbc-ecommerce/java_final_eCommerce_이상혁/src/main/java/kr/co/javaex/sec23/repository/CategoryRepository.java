package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    // 전체 카테고리 목록 조회
    public List<Category> findAll(Connection con) throws SQLException {
        List<Category> categories = new ArrayList<>();

        String sql = """
                SELECT CATEGORY_NO, CATEGORY_ID, PARENT_NO, CATEGORY_NAME, SORT_ORDER
                FROM CATEGORIES
                ORDER BY SORT_ORDER, CATEGORY_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categories.add(mapToCategory(rs));
            }
        }

        return categories;
    }

    // 카테고리 번호로 조회
    public Category findByCategoryNo(Connection con, int categoryNo) throws SQLException {
        String sql = """
                SELECT CATEGORY_NO, CATEGORY_ID, PARENT_NO, CATEGORY_NAME, SORT_ORDER
                FROM CATEGORIES
                WHERE CATEGORY_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, categoryNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToCategory(rs);
                }
            }
        }

        return null;
    }

    // 카테고리 ID로 조회
    public Category findByCategoryId(Connection con, String categoryId) throws SQLException {
        String sql = """
                SELECT CATEGORY_NO, CATEGORY_ID, PARENT_NO, CATEGORY_NAME, SORT_ORDER
                FROM CATEGORIES
                WHERE CATEGORY_ID = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, categoryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToCategory(rs);
                }
            }
        }

        return null;
    }

    // 대분류 카테고리 목록 조회
    public List<Category> findParents(Connection con) throws SQLException {
        List<Category> categories = new ArrayList<>();

        String sql = """
                SELECT CATEGORY_NO, CATEGORY_ID, PARENT_NO, CATEGORY_NAME, SORT_ORDER
                FROM CATEGORIES
                WHERE PARENT_NO IS NULL
                ORDER BY SORT_ORDER, CATEGORY_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categories.add(mapToCategory(rs));
            }
        }

        return categories;
    }

    // 중분류 카테고리 목록 조회
    public List<Category> findChildren(Connection con) throws SQLException {
        List<Category> categories = new ArrayList<>();

        String sql = """
                SELECT CATEGORY_NO, CATEGORY_ID, PARENT_NO, CATEGORY_NAME, SORT_ORDER
                FROM CATEGORIES
                WHERE PARENT_NO IS NOT NULL
                ORDER BY SORT_ORDER, CATEGORY_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categories.add(mapToCategory(rs));
            }
        }

        return categories;
    }

    // 카테고리 등록
    public void save(Connection con, Category category) throws SQLException {
        String sql = """
                INSERT INTO CATEGORIES (
                    CATEGORY_NO, CATEGORY_ID, PARENT_NO, CATEGORY_NAME, SORT_ORDER
                ) VALUES (
                    SEQ_CATEGORIES.NEXTVAL, ?, ?, ?, ?
                )
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, category.getCategoryId());

            if (category.getParentNo() == null) {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(2, category.getParentNo());
            }

            pstmt.setString(3, category.getCategoryName());
            pstmt.setInt(4, category.getSortOrder());

            pstmt.executeUpdate();
        }
    }

    // 카테고리 수정
    public void update(Connection con, Category category) throws SQLException {
        String sql = """
                UPDATE CATEGORIES
                SET PARENT_NO = ?,
                    CATEGORY_NAME = ?,
                    SORT_ORDER = ?
                WHERE CATEGORY_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            if (category.getParentNo() == null) {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(1, category.getParentNo());
            }

            pstmt.setString(2, category.getCategoryName());
            pstmt.setInt(3, category.getSortOrder());
            pstmt.setInt(4, category.getCategoryNo());

            pstmt.executeUpdate();
        }
    }

    // 카테고리 삭제
    public void deleteByCategoryNo(Connection con, int categoryNo) throws SQLException {
        String sql = "DELETE FROM CATEGORIES WHERE CATEGORY_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, categoryNo);
            pstmt.executeUpdate();
        }
    }

    // 하위 카테고리 존재 여부 확인
    public boolean hasChildren(Connection con, int parentNo) throws SQLException {
        String sql = """
                SELECT 1
                FROM DUAL
                WHERE EXISTS (
                    SELECT 1
                    FROM CATEGORIES
                    WHERE PARENT_NO = ?
                )
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, parentNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Category mapToCategory(ResultSet rs) throws SQLException {
        int parentNoValue = rs.getInt("PARENT_NO");
        boolean isParentNoNull = rs.wasNull();

        return Category.builder()
                .categoryNo(rs.getInt("CATEGORY_NO"))
                .categoryId(rs.getString("CATEGORY_ID"))
                .parentNo(isParentNoNull ? null : parentNoValue)
                .categoryName(rs.getString("CATEGORY_NAME"))
                .sortOrder(rs.getInt("SORT_ORDER"))
                .build();
    }
}