package kr.co.javaex.sec23.domain;

import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;

import java.util.Date;

public class Product {

    private int productNo;
    private String productId;
    private String productName;
    private String productDescription;
    private int price;
    private int stockQuantity;
    private ProductStatus productStatus;
    private Date createdAt;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int productNo;
        private String productId;
        private String productName;
        private String productDescription;
        private int price;
        private int stockQuantity;
        private ProductStatus productStatus;
        private Date createdAt;

        public Builder productNo(int productNo) {
            this.productNo = productNo;
            return this;
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder productDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Builder stockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public Builder productStatus(ProductStatus productStatus) {
            this.productStatus = productStatus;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.productNo = this.productNo;
            product.productId = this.productId;
            product.productName = this.productName;
            product.productDescription = this.productDescription;
            product.price = this.price;
            product.stockQuantity = this.stockQuantity;
            product.productStatus = this.productStatus;
            product.createdAt = this.createdAt;
            return product;
        }
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
