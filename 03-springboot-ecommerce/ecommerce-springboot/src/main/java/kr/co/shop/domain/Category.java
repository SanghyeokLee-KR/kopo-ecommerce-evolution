package kr.co.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_category")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_category_seq_generator", sequenceName = "seq_tb_category", allocationSize = 1)
public class Category {

    // 카테고리 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_category_seq_generator")
    @Column(name = "nb_category")
    private Long nbCategory;

    // 상위 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_parent_category")
    private Category parentCategory;

    // 카테고리명
    @Column(name = "nm_category", nullable = false, length = 100)
    private String nmCategory;

    // 카테고리 레벨
    @Column(name = "cn_level", nullable = false)
    private Integer cnLevel;

    // 정렬 순서
    @Column(name = "cn_order", nullable = false)
    private Integer cnOrder;

    // 카테고리 정보 수정
    public void updateCategory(
            Category parentCategory,
            String nmCategory,
            Integer cnLevel,
            Integer cnOrder
    ) {
        this.parentCategory = parentCategory;
        this.nmCategory = nmCategory;
        this.cnLevel = cnLevel;
        this.cnOrder = cnOrder;
    }
}