package kr.co.javaex.sec23.domain;

/**
 * <h3>카테고리 클래스</h3>
 */
public class Category {
    private String categoryId;
    private String parentCategoryId;
    private String categoryName;
    private int sortOrder;

    public Category() {
    }

    public Category(String categoryId, String parentCategoryId, String categoryName, int sortOrder) {
        this.categoryId = categoryId;
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
        this.sortOrder = sortOrder;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (순서: %d, 상위: %s)", categoryId, categoryName, sortOrder, parentCategoryId == null ? "대분류" : parentCategoryId);
    }
}