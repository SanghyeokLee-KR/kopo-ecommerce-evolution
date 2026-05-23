package kr.co.javaex.sec23.domain;

public class ProductCategoryMapping {

    private int mappingNo;
    private int productNo;
    private int categoryNo;
    private int sortOrder;

    private String productName;
    private String categoryName;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int mappingNo;
        private int productNo;
        private int categoryNo;
        private int sortOrder;

        private String productName;
        private String categoryName;

        public Builder mappingNo(int mappingNo) {
            this.mappingNo = mappingNo;
            return this;
        }

        public Builder productNo(int productNo) {
            this.productNo = productNo;
            return this;
        }

        public Builder categoryNo(int categoryNo) {
            this.categoryNo = categoryNo;
            return this;
        }

        public Builder sortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public ProductCategoryMapping build() {
            ProductCategoryMapping mapping = new ProductCategoryMapping();
            mapping.mappingNo = this.mappingNo;
            mapping.productNo = this.productNo;
            mapping.categoryNo = this.categoryNo;
            mapping.sortOrder = this.sortOrder;
            mapping.productName = this.productName;
            mapping.categoryName = this.categoryName;
            return mapping;
        }
    }

    public int getMappingNo() {
        return mappingNo;
    }

    public void setMappingNo(int mappingNo) {
        this.mappingNo = mappingNo;
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}