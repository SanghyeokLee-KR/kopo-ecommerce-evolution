package kr.co.javaex.sec23.domain;


import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;

/**
 * <h3>상품 클래스</h3>
 */
public class Product {

    private String productId;
    private String productName;
    private String productDescription;
    private int price;
    private int stockQuantity;
    private ProductStatus productStatus;

    public Product() {
    }

    public Product(String productId, String productName, String productDescription, int price, int stockQuantity, ProductStatus productStatus) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.productStatus = productStatus;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}