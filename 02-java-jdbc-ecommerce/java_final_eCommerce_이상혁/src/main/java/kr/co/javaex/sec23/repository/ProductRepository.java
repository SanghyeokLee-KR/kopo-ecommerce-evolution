package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public List<Product> findAll(Connection con) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT PRODUCT_NO, PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC,
                       PRICE, STOCK_QTY, PRODUCT_STATUS, CREATED_AT
                FROM PRODUCTS
                ORDER BY PRODUCT_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapToProduct(rs));
            }
        }

        return products;
    }

    public Product findByProductNo(Connection con, int productNo) throws SQLException {
        String sql = """
                SELECT PRODUCT_NO, PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC,
                       PRICE, STOCK_QTY, PRODUCT_STATUS, CREATED_AT
                FROM PRODUCTS
                WHERE PRODUCT_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, productNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToProduct(rs);
                }
            }
        }

        return null;
    }

    public Product findByProductId(Connection con, String productId) throws SQLException {
        String sql = """
                SELECT PRODUCT_NO, PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC,
                       PRICE, STOCK_QTY, PRODUCT_STATUS, CREATED_AT
                FROM PRODUCTS
                WHERE PRODUCT_ID = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToProduct(rs);
                }
            }
        }

        return null;
    }

    public List<Product> findAllAvailable(Connection con) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT PRODUCT_NO, PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC,
                       PRICE, STOCK_QTY, PRODUCT_STATUS, CREATED_AT
                FROM PRODUCTS
                WHERE PRODUCT_STATUS <> ?
                ORDER BY PRODUCT_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, ProductStatus.판매중지.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    public List<Product> findAllOrderByPriceAsc(Connection con) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT PRODUCT_NO, PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC,
                       PRICE, STOCK_QTY, PRODUCT_STATUS, CREATED_AT
                FROM PRODUCTS
                WHERE PRODUCT_STATUS <> ?
                ORDER BY PRICE ASC, PRODUCT_NO ASC
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, ProductStatus.판매중지.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    public List<Product> findAllOrderByPriceDesc(Connection con) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT PRODUCT_NO, PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC,
                       PRICE, STOCK_QTY, PRODUCT_STATUS, CREATED_AT
                FROM PRODUCTS
                WHERE PRODUCT_STATUS <> ?
                ORDER BY PRICE DESC, PRODUCT_NO ASC
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, ProductStatus.판매중지.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    public List<Product> searchByName(Connection con, String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT PRODUCT_NO, PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC,
                       PRICE, STOCK_QTY, PRODUCT_STATUS, CREATED_AT
                FROM PRODUCTS
                WHERE PRODUCT_STATUS <> ?
                  AND PRODUCT_NAME LIKE ?
                ORDER BY PRODUCT_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, ProductStatus.판매중지.name());
            pstmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    public List<Product> findByCategoryNo(Connection con, int categoryNo) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT P.PRODUCT_NO, P.PRODUCT_ID, P.PRODUCT_NAME, P.PRODUCT_DESC,
                       P.PRICE, P.STOCK_QTY, P.PRODUCT_STATUS, P.CREATED_AT
                FROM PRODUCTS P
                INNER JOIN PRODUCT_CATEGORY_MAPPING PCM
                    ON P.PRODUCT_NO = PCM.PRODUCT_NO
                WHERE PCM.CATEGORY_NO = ?
                  AND P.PRODUCT_STATUS <> ?
                ORDER BY PCM.SORT_ORDER ASC, P.PRODUCT_NO ASC
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, categoryNo);
            pstmt.setString(2, ProductStatus.판매중지.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    public void save(Connection con, Product product) throws SQLException {
        String sql = """
                INSERT INTO PRODUCTS (
                    PRODUCT_NO, PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC,
                    PRICE, STOCK_QTY, PRODUCT_STATUS, CREATED_AT
                ) VALUES (
                    SEQ_PRODUCTS.NEXTVAL, ?, ?, ?, ?, ?, ?, SYSDATE
                )
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getProductDescription());
            pstmt.setInt(4, product.getPrice());
            pstmt.setInt(5, product.getStockQuantity());
            pstmt.setString(6, product.getProductStatus().name());

            pstmt.executeUpdate();
        }
    }

    public void update(Connection con, Product product) throws SQLException {
        String sql = """
                UPDATE PRODUCTS
                SET PRODUCT_NAME = ?,
                    PRODUCT_DESC = ?,
                    PRICE = ?,
                    STOCK_QTY = ?,
                    PRODUCT_STATUS = ?
                WHERE PRODUCT_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getProductDescription());
            pstmt.setInt(3, product.getPrice());
            pstmt.setInt(4, product.getStockQuantity());
            pstmt.setString(5, product.getProductStatus().name());
            pstmt.setInt(6, product.getProductNo());

            pstmt.executeUpdate();
        }
    }

    public void updateStatus(Connection con, int productNo, ProductStatus status) throws SQLException {
        String sql = "UPDATE PRODUCTS SET PRODUCT_STATUS = ? WHERE PRODUCT_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setInt(2, productNo);
            pstmt.executeUpdate();
        }
    }

    public void updateStock(Connection con, int productNo, int stockQty, ProductStatus status) throws SQLException {
        String sql = """
                UPDATE PRODUCTS
                SET STOCK_QTY = ?, PRODUCT_STATUS = ?
                WHERE PRODUCT_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, stockQty);
            pstmt.setString(2, status.name());
            pstmt.setInt(3, productNo);
            pstmt.executeUpdate();
        }
    }

    public void deleteByProductNo(Connection con, int productNo) throws SQLException {
        String sql = "DELETE FROM PRODUCTS WHERE PRODUCT_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, productNo);
            pstmt.executeUpdate();
        }
    }

    private Product mapToProduct(ResultSet rs) throws SQLException {
        return Product.builder()
                .productNo(rs.getInt("PRODUCT_NO"))
                .productId(rs.getString("PRODUCT_ID"))
                .productName(rs.getString("PRODUCT_NAME"))
                .productDescription(rs.getString("PRODUCT_DESC"))
                .price(rs.getInt("PRICE"))
                .stockQuantity(rs.getInt("STOCK_QTY"))
                .productStatus(ProductStatus.valueOf(rs.getString("PRODUCT_STATUS")))
                .createdAt(rs.getDate("CREATED_AT"))
                .build();
    }
}