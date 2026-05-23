package kr.co.javaex.sec23.domain;

public class Cart {

    private int cartNo;
    private int userNo;
    private int productNo;
    private int quantity;

    // 상품 테이블 추가 필드
    private String productName;
    private int price;
    private String productId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int cartNo;
        private int userNo;
        private int productNo;
        private int quantity;

        private String productName;
        private int price;
        private String productId;

        public Builder cartNo(int cartNo) {
            this.cartNo = cartNo;
            return this;
        }

        public Builder userNo(int userNo) {
            this.userNo = userNo;
            return this;
        }

        public Builder productNo(int productNo) {
            this.productNo = productNo;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Cart build() {
            Cart cart = new Cart();
            cart.cartNo = this.cartNo;
            cart.userNo = this.userNo;
            cart.productNo = this.productNo;
            cart.quantity = this.quantity;

            cart.productName = this.productName;
            cart.price = this.price;
            cart.productId = this.productId;

            return cart;
        }
    }

    public int getCartNo() {
        return cartNo;
    }

    public void setCartNo(int cartNo) {
        this.cartNo = cartNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public String getProductId() {
        return productId;
    }
}