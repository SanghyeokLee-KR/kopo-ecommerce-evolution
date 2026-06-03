package kr.co.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 장바구니 품목 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_basket_item", uniqueConstraints = {@UniqueConstraint(name = "uk_tb_basket_item_product", columnNames = {"nb_basket", "nb_product"})})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_basket_item_seq_generator", sequenceName = "seq_tb_basket_item", allocationSize = 1)
public class BasketItem {

    // 장바구니 품목 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_basket_item_seq_generator")
    @Column(name = "nb_basket_item")
    private Long nbBasketItem;

    // 장바구니
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_basket", nullable = false)
    private Basket basket;

    // 품목 순번
    @Column(name = "cn_basket_item_order", nullable = false)
    private Integer cnBasketItemOrder;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_product", nullable = false)
    private Product product;

    // 품목 단가
    @Column(name = "qt_basket_item_price")
    private Integer qtBasketItemPrice;

    // 품목 수량
    @Column(name = "qt_basket_item")
    private Integer qtBasketItem;

    // 품목 금액
    @Column(name = "qt_basket_item_amount")
    private Integer qtBasketItemAmount;

    // 장바구니 상품 수량 변경
    public void changeQuantity(Integer quantity) {
        this.qtBasketItem = quantity;
        this.qtBasketItemAmount = this.qtBasketItemPrice * quantity;
    }

    // 장바구니 상품 수량 증가
    public void increaseQuantity(Integer quantity) {
        this.qtBasketItem += quantity;
        this.qtBasketItemAmount = this.qtBasketItemPrice * this.qtBasketItem;
    }
}