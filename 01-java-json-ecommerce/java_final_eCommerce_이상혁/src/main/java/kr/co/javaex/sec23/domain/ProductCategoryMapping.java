package kr.co.javaex.sec23.domain;


/**
 * <h3>상품카테고리 클래스</h3>
 */
public class ProductCategoryMapping {

    private String productId;
    private String categoryId;
    private int sortOrder;

    public ProductCategoryMapping() {
    }

    public ProductCategoryMapping(String productId, String categoryId, int sortOrder) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.sortOrder = sortOrder;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}