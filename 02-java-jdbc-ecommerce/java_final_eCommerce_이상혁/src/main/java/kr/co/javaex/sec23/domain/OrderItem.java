package kr.co.javaex.sec23.domain;

public class OrderItem {

    private int orderItemNo;
    private int orderNo;
    private int productNo;
    private String productName;
    private int orderPrice;
    private int quantity;
    private int itemTotalPrice;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int orderItemNo;
        private int orderNo;
        private int productNo;
        private String productName;
        private int orderPrice;
        private int quantity;
        private int itemTotalPrice;

        public Builder orderItemNo(int orderItemNo) {
            this.orderItemNo = orderItemNo;
            return this;
        }

        public Builder orderNo(int orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public Builder productNo(int productNo) {
            this.productNo = productNo;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder orderPrice(int orderPrice) {
            this.orderPrice = orderPrice;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder itemTotalPrice(int itemTotalPrice) {
            this.itemTotalPrice = itemTotalPrice;
            return this;
        }

        public OrderItem build() {
            OrderItem orderItem = new OrderItem();
            orderItem.orderItemNo = this.orderItemNo;
            orderItem.orderNo = this.orderNo;
            orderItem.productNo = this.productNo;
            orderItem.productName = this.productName;
            orderItem.orderPrice = this.orderPrice;
            orderItem.quantity = this.quantity;
            orderItem.itemTotalPrice = this.itemTotalPrice;
            return orderItem;
        }
    }

    public int getOrderItemNo() {
        return orderItemNo;
    }

    public void setOrderItemNo(int orderItemNo) {
        this.orderItemNo = orderItemNo;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(int itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }
}