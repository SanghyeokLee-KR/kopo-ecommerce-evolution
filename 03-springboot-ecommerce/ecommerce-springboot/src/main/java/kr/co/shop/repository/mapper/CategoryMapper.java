package kr.co.shop.repository.mapper;

import kr.co.shop.dto.category.response.CategoryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 카테고리 조회 Mapper
 */
@Mapper
public interface CategoryMapper {

    // 카테고리 목록 조회
    List<CategoryResponse> findAllCategories();
}