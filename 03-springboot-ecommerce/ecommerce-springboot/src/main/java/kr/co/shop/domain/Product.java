package kr.co.shop.domain;

import jakarta.persistence.*;
import kr.co.shop.common.exception.BusinessException;
import kr.co.shop.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_product")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_product_seq_generator", sequenceName = "seq_tb_product", allocationSize = 1)
public class Product {

    // 상품 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_product_seq_generator")
    @Column(name = "nb_product")
    private Long nbProduct;

    // 상품 코드
    @Column(name = "no_product", nullable = false, unique = true, length = 30)
    private String noProduct;

    // 상품명
    @Column(name = "nm_product", nullable = false, length = 200)
    private String nmProduct;

    // 상세 설명
    @Column(name = "nm_detail_explain", length = 4000)
    private String nmDetailExplain;

    // 상품 이미지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_file")
    private Content content;

    // 판매 시작일 (yyyyMMdd)
    @Column(name = "dt_start_date", length = 8)
    private String dtStartDate;

    // 판매 종료일 (yyyyMMdd)
    @Column(name = "dt_end_date", length = 8)
    private String dtEndDate;

    // 판매 가격
    @Column(name = "qt_sale_price", nullable = false)
    private Integer qtSalePrice;

    // 재고 수량
    @Column(name = "qt_stock")
    private Integer qtStock;

    // 상품 정보 수정
    public void updateProduct(
            String nmProduct,
            String nmDetailExplain,
            String dtStartDate,
            String dtEndDate,
            Integer qtSalePrice,
            Integer qtStock
    ) {
        this.nmProduct = nmProduct;
        this.nmDetailExplain = nmDetailExplain;
        this.dtStartDate = dtStartDate;
        this.dtEndDate = dtEndDate;
        this.qtSalePrice = qtSalePrice;
        this.qtStock = qtStock;
    }

    // 상품 이미지 변경
    public void changeContent(Content content) {
        this.content = content;
    }

    // 상품 재고 차감
    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_STOCK);
        }

        if (this.qtStock == null || this.qtStock <= 0) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }

        if (this.qtStock < quantity) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_STOCK);
        }

        this.qtStock -= quantity;
    }
}