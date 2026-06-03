package kr.co.shop.service;

import kr.co.shop.dto.category.request.CategoryCreateRequest;
import kr.co.shop.dto.category.request.CategoryUpdateRequest;
import kr.co.shop.dto.category.response.CategoryResponse;

import java.util.List;

/**
 * 카테고리 서비스
 */
public interface CategoryService {

    /**
     * 전체 카테고리 목록 조회
     */
    List<CategoryResponse> findAll();

    /**
     * 카테고리 등록
     */
    void create(CategoryCreateRequest request);

    /**
     * 카테고리 수정
     */
    void update(Long nbCategory, CategoryUpdateRequest request);

    /**
     * 카테고리 삭제
     */
    void delete(Long nbCategory);
}