package kr.co.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 품목 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_order_item")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_order_item_seq_generator", sequenceName = "seq_tb_order_item", allocationSize = 1)
public class OrderItem {

    // 주문 품목 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_order_item_seq_generator")
    @Column(name = "nb_order_item")
    private Long nbOrderItem;

    // 주문
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_order", nullable = false)
    private Order order;

    // 주문 품목 순번
    @Column(name = "cn_order_item", nullable = false)
    private Integer cnOrderItem;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_product", nullable = false)
    private Product product;

    // 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_user", nullable = false)
    private User user;

    // 주문 단가
    @Column(name = "qt_unit_price", nullable = false)
    private Integer qtUnitPrice;

    // 주문 수량
    @Column(name = "qt_order_item", nullable = false)
    private Integer qtOrderItem;
}