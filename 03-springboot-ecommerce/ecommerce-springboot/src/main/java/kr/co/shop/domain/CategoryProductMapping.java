package kr.co.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품과 카테고리 매핑 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_category_product_mapping", uniqueConstraints = {@UniqueConstraint(name = "uk_tcpm_category_product", columnNames = {"nb_category", "nb_product"})})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_category_product_mapping_seq_generator", sequenceName = "seq_tb_category_product_mapping", allocationSize = 1)
public class CategoryProductMapping {

    // 매핑 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_category_product_mapping_seq_generator")
    @Column(name = "nb_category_product_mapping")
    private Long nbCategoryProductMapping;

    // 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_category", nullable = false)
    private Category category;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_product", nullable = false)
    private Product product;

    // 정렬 순서
    @Column(name = "cn_order", nullable = false)
    private Integer cnOrder;

    // 카테고리 변경
    public void changeCategory(Category category) {
        this.category = category;
    }
}