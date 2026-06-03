package kr.co.shop.repository.jpa;

import kr.co.shop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 카테고리 Repository
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 대분류 정렬 순서 중복 확인
    boolean existsByParentCategoryIsNullAndCnLevelAndCnOrder(
            Integer cnLevel,
            Integer cnOrder
    );

    // 중분류 정렬 순서 중복 확인
    boolean existsByParentCategory_NbCategoryAndCnLevelAndCnOrder(
            Long nbParentCategory,
            Integer cnLevel,
            Integer cnOrder
    );

    // 수정 시 자기 자신을 제외한 대분류 정렬 순서 중복 확인
    @Query("""
           SELECT COUNT(c) > 0
           FROM Category c
           WHERE c.parentCategory IS NULL
             AND c.cnLevel = :cnLevel
             AND c.cnOrder = :cnOrder
             AND c.nbCategory <> :nbCategory
           """)
    boolean existsDuplicateParentOrderForUpdate(
            @Param("nbCategory") Long nbCategory,
            @Param("cnLevel") Integer cnLevel,
            @Param("cnOrder") Integer cnOrder
    );

    // 수정 시 자기 자신을 제외한 중분류 정렬 순서 중복 확인
    @Query("""
           SELECT COUNT(c) > 0
           FROM Category c
           WHERE c.parentCategory.nbCategory = :nbParentCategory
             AND c.cnLevel = :cnLevel
             AND c.cnOrder = :cnOrder
             AND c.nbCategory <> :nbCategory
           """)
    boolean existsDuplicateChildOrderForUpdate(
            @Param("nbCategory") Long nbCategory,
            @Param("nbParentCategory") Long nbParentCategory,
            @Param("cnLevel") Integer cnLevel,
            @Param("cnOrder") Integer cnOrder
    );
}