package kr.co.javaex.sec23.domain;

import kr.co.javaex.sec23.util.common.enums.order.OrderStatus;

import java.util.Date;

public class Order {

    private int orderNo;
    private int userNo;
    private String userName;
    private OrderStatus orderStatus;
    private Date orderDate;
    private int totalPrice;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int orderNo;
        private int userNo;
        private String userName;
        private OrderStatus orderStatus;
        private Date orderDate;
        private int totalPrice;

        public Builder orderNo(int orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public Builder userNo(int userNo) {
            this.userNo = userNo;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderDate(Date orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder totalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.orderNo = this.orderNo;
            order.userNo = this.userNo;
            order.userName = this.userName;
            order.orderStatus = this.orderStatus;
            order.orderDate = this.orderDate;
            order.totalPrice = this.totalPrice;
            return order;
        }
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}