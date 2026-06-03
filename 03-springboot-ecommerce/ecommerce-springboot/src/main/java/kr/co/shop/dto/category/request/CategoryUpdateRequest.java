package kr.co.shop.dto.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카테고리 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryUpdateRequest {

    @NotBlank
    private String nmCategory;

    private Long nbParentCategory;

    private Integer cnLevel;

    @NotNull
    private Integer cnOrder;
}