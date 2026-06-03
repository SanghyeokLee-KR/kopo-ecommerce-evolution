package kr.co.shop.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * 상품 등록/수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "상품명을 입력해주세요.")
    private String nmProduct;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String dcProduct;

    @NotNull(message = "판매 일자를 입력해주세요.")
    private LocalDate dtSellStart;

    @NotNull(message = "가격을 입력해주세요.")
    @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
    private Integer qtPrice;

    @NotNull(message = "재고 수량을 입력해주세요.")
    @PositiveOrZero(message = "재고 수량은 0개 이상이어야 합니다.")
    private Integer qtStock;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long nbCategory;

    private MultipartFile productImage;
}