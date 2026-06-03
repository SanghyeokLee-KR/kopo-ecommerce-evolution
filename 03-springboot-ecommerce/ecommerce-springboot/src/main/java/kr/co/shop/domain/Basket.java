package kr.co.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 장바구니 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_basket", uniqueConstraints = {@UniqueConstraint(name = "uk_tb_basket_user", columnNames = "nb_user")})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_basket_seq_generator", sequenceName = "seq_tb_basket", allocationSize = 1)
public class Basket {

    // 장바구니 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_basket_seq_generator")
    @Column(name = "nb_basket")
    private Long nbBasket;

    // 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_user", nullable = false)
    private User user;
}