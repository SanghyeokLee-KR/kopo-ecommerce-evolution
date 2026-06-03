package kr.co.shop.service;

import kr.co.shop.common.exception.BusinessException;
import kr.co.shop.common.exception.ErrorCode;
import kr.co.shop.domain.Category;
import kr.co.shop.dto.category.request.CategoryCreateRequest;
import kr.co.shop.dto.category.request.CategoryUpdateRequest;
import kr.co.shop.dto.category.response.CategoryResponse;
import kr.co.shop.repository.jpa.CategoryRepository;
import kr.co.shop.repository.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "categoryCache")
    public List<CategoryResponse> findAll() {
        // 카테고리 등록/수정/삭제 시 @CacheEvict로 무효화
        return categoryMapper.findAllCategories();
    }

    @Override
    @Transactional
    @CacheEvict(value = "categoryCache", allEntries = true)
    public void create(CategoryCreateRequest request) {
        validateCategoryLevel(request.getNbParentCategory(), request.getCnLevel());
        validateDuplicateOrderForCreate(request.getNbParentCategory(), request.getCnLevel(), request.getCnOrder());

        Category parentCategory = null;
        if (request.getNbParentCategory() != null) {
            parentCategory = categoryRepository.findById(request.getNbParentCategory())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        Category category = categoryRepository.save(
                Category.builder()
                        .parentCategory(parentCategory)
                        .nmCategory(request.getNmCategory())
                        .cnLevel(request.getCnLevel())
                        .cnOrder(request.getCnOrder())
                        .build()
        );

        log.info("카테고리 등록 완료 - nbCategory={}, nmCategory={}", category.getNbCategory(), category.getNmCategory());
    }

    @Override
    @Transactional
    @CacheEvict(value = "categoryCache", allEntries = true)
    public void update(Long nbCategory, CategoryUpdateRequest request) {
        validateCategoryLevel(request.getNbParentCategory(), request.getCnLevel());

        Category category = categoryRepository.findById(nbCategory)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        validateDuplicateOrderForUpdate(nbCategory, request.getNbParentCategory(), request.getCnLevel(), request.getCnOrder());

        Category parentCategory = null;
        if (request.getNbParentCategory() != null) {
            parentCategory = categoryRepository.findById(request.getNbParentCategory())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        category.updateCategory(parentCategory, request.getNmCategory(), request.getCnLevel(), request.getCnOrder());

        log.info("카테고리 수정 완료 - nbCategory={}, nmCategory={}", nbCategory, request.getNmCategory());
    }

    @Override
    @Transactional
    @CacheEvict(value = "categoryCache", allEntries = true)
    public void delete(Long nbCategory) {
        Category category = categoryRepository.findById(nbCategory)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // TODO: 하위 카테고리나 연결된 상품이 있을 때 예외 처리 필요. (DB FK 제약만 사용)
        categoryRepository.delete(category);

        log.info("카테고리 삭제 완료 - nbCategory={}", nbCategory);
    }

    private void validateDuplicateOrderForCreate(Long nbParentCategory, Integer cnLevel, Integer cnOrder) {
        boolean duplicated = (nbParentCategory == null)
                ? categoryRepository.existsByParentCategoryIsNullAndCnLevelAndCnOrder(cnLevel, cnOrder)
                : categoryRepository.existsByParentCategory_NbCategoryAndCnLevelAndCnOrder(nbParentCategory, cnLevel, cnOrder);

        if (duplicated) {
            log.warn("카테고리 순서 중복 - nbParentCategory={}, cnLevel={}, cnOrder={}", nbParentCategory, cnLevel, cnOrder);
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_ORDER);
        }
    }

    private void validateDuplicateOrderForUpdate(Long nbCategory, Long nbParentCategory, Integer cnLevel, Integer cnOrder) {
        boolean duplicated = (nbParentCategory == null)
                ? categoryRepository.existsDuplicateParentOrderForUpdate(nbCategory, cnLevel, cnOrder)
                : categoryRepository.existsDuplicateChildOrderForUpdate(nbCategory, nbParentCategory, cnLevel, cnOrder);

        if (duplicated) {
            log.warn("카테고리 순서 중복 (수정) - nbCategory={}, nbParentCategory={}, cnOrder={}", nbCategory, nbParentCategory, cnOrder);
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_ORDER);
        }
    }

    // 레벨 1: 최상위(부모 없음), 레벨 2: 하위(부모 필수) -> 1, 2만 있음
    private void validateCategoryLevel(Long nbParentCategory, Integer cnLevel) {
        if (cnLevel == null || (cnLevel != 1 && cnLevel != 2)) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        if (cnLevel == 1 && nbParentCategory != null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        if (cnLevel == 2 && nbParentCategory == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }
}