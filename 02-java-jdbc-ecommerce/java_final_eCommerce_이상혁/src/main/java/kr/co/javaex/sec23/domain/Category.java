package kr.co.javaex.sec23.domain;

public class Category {

    private int categoryNo;
    private String categoryId;
    private Integer parentNo;
    private String categoryName;
    private int sortOrder;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int categoryNo;
        private String categoryId;
        private Integer parentNo;
        private String categoryName;
        private int sortOrder;

        public Builder categoryNo(int categoryNo) {
            this.categoryNo = categoryNo;
            return this;
        }

        public Builder categoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder parentNo(Integer parentNo) {
            this.parentNo = parentNo;
            return this;
        }

        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder sortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.categoryNo = this.categoryNo;
            category.categoryId = this.categoryId;
            category.parentNo = this.parentNo;
            category.categoryName = this.categoryName;
            category.sortOrder = this.sortOrder;
            return category;
        }
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getParentNo() {
        return parentNo;
    }

    public void setParentNo(Integer parentNo) {
        this.parentNo = parentNo;
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
        return String.format("[%s] %s (순서: %d, 상위번호: %s)",
                categoryId,
                categoryName,
                sortOrder,
                parentNo == null ? "대분류" : parentNo);
    }
}
