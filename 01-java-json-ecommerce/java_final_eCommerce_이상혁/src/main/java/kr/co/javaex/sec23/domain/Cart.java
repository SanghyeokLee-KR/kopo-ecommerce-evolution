package kr.co.javaex.sec23.domain;

/**
 * <h3>카트 클래스</h3>
 */
public class Cart {

    private String userId;
    private String productId;
    private int quantity;

    public Cart() {
    }

    public Cart(String userId, String productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}