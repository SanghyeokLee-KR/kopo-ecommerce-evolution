package kr.co.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 주문 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_order")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_order_seq_generator", sequenceName = "seq_tb_order", allocationSize = 1)
public class Order {

    // 주문 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_order_seq_generator")
    @Column(name = "nb_order")
    private Long nbOrder;

    // 주문 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_user", nullable = false)
    private User user;

    // 주문 금액
    @Column(name = "qt_order_amount")
    private Integer qtOrderAmount;

    // 주문자명
    @Column(name = "nm_order_person", length = 100)
    private String nmOrderPerson;

    // 배송 주소
    @Column(name = "nm_delivery_address", length = 200)
    private String nmDeliveryAddress;

    // 주문 일시
    @Column(name = "da_order")
    private LocalDateTime daOrder;

    // 주문 상태
    @Column(name = "st_order", length = 4)
    private String stOrder;

    // 주문 상태 변경
    public void changeStatus(String stOrder) {
        this.stOrder = stOrder;
    }
}