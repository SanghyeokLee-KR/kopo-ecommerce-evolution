package kr.co.shop.dto.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카테고리 등록 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryCreateRequest {

    @NotBlank
    private String nmCategory;

    private Long nbParentCategory;

    private Integer cnLevel;

    @NotNull
    private Integer cnOrder;
}