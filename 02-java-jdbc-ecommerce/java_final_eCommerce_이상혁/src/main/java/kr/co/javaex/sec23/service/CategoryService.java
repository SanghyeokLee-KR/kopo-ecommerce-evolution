package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Category;
import kr.co.javaex.sec23.repository.CategoryRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository = new CategoryRepository();

    // 카테고리 목록 조회
    public void showAllCategories() {
        try (Connection con = DBUtil.getConnection()) {
            List<Category> categories = categoryRepository.findAll(con);

            if (categories.isEmpty()) {
                System.out.println("카테고리가 없습니다.");
                return;
            }

            List<Category> parentCategories = new ArrayList<>();

            for (Category category : categories) {
                if (category.getParentNo() == null) {
                    parentCategories.add(category);
                }
            }

            parentCategories.sort(Comparator.comparingInt(Category::getSortOrder));

            System.out.println("\n===== 카테고리 목록 =====");

            for (Category parentCategory : parentCategories) {
                System.out.println(parentCategory.getCategoryName() + " (" +
                        parentCategory.getCategoryId() + ", 정렬순서: " +
                        parentCategory.getSortOrder() + ")");

                List<Category> childCategories = new ArrayList<>();

                for (Category category : categories) {
                    if (category.getParentNo() != null
                            && category.getParentNo().equals(parentCategory.getCategoryNo())) {
                        childCategories.add(category);
                    }
                }

                childCategories.sort(Comparator.comparingInt(Category::getSortOrder));

                for (Category childCategory : childCategories) {
                    System.out.println("  └ " + childCategory.getCategoryName() + " (" +
                            childCategory.getCategoryId() + ", 정렬순서: " +
                            childCategory.getSortOrder() + ")");
                }
            }

        } catch (Exception e) {
            System.out.println("카테고리 조회 실패: " + e.getMessage());
            throw new RuntimeException("카테고리 조회 실패", e);
        }
    }

    // 카테고리 등록
    public void addCategory() {
        System.out.println("\n===== 카테고리 등록 =====");

        String categoryId = InputUtil.inputRequiredLine("카테고리 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            if (categoryRepository.findByCategoryId(con, categoryId) != null) {
                System.out.println("이미 존재하는 카테고리 ID입니다.");
                return;
            }

            System.out.println("1. 대분류");
            System.out.println("2. 중분류");
            int categoryType = InputUtil.inputInt("카테고리 종류 선택: ");

            Integer parentNo = null;

            switch (categoryType) {
                case 1:
                    parentNo = null;
                    break;
                case 2:
                    parentNo = selectParentCategoryNo(con);
                    if (parentNo == null) {
                        return;
                    }
                    break;
                default:
                    System.out.println("잘못된 번호입니다.");
                    return;
            }

            String categoryName = InputUtil.inputRequiredLine("카테고리명 입력: ");
            int sortOrder = InputUtil.inputPositiveInt("정렬 순서 입력: ");

            Category newCategory = Category.builder()
                    .categoryId(categoryId)
                    .parentNo(parentNo)
                    .categoryName(categoryName)
                    .sortOrder(sortOrder)
                    .build();

            categoryRepository.save(con, newCategory);

            con.commit();
            System.out.println("카테고리 등록 완료");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("카테고리 등록 실패: " + e.getMessage());
            throw new RuntimeException("카테고리 등록 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 카테고리 수정
    public void updateCategory() {
        System.out.println("\n===== 카테고리 수정 =====");

        String categoryId = InputUtil.inputRequiredLine("수정할 카테고리 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Category targetCategory = categoryRepository.findByCategoryId(con, categoryId);

            if (targetCategory == null) {
                System.out.println("카테고리가 없습니다.");
                return;
            }

            System.out.println("1. 대분류");
            System.out.println("2. 중분류");
            int categoryType = InputUtil.inputInt("카테고리 종류 선택: ");

            Integer parentNo = null;

            switch (categoryType) {
                case 1:
                    parentNo = null;
                    break;
                case 2:
                    parentNo = selectParentCategoryNo(con);
                    if (parentNo == null) {
                        return;
                    }

                    if (targetCategory.getCategoryNo() == parentNo) {
                        System.out.println("자기를 상위 카테고리로 설정할 수 없습니다.");
                        return;
                    }
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    return;
            }

            String categoryName = InputUtil.inputRequiredLine("새 카테고리명: ");
            int sortOrder = InputUtil.inputPositiveInt("새 정렬 순서: ");

            targetCategory.setParentNo(parentNo);
            targetCategory.setCategoryName(categoryName);
            targetCategory.setSortOrder(sortOrder);

            categoryRepository.update(con, targetCategory);

            con.commit();
            System.out.println("수정 완료");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("카테고리 수정 실패: " + e.getMessage());
            throw new RuntimeException("카테고리 수정 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 카테고리 삭제
    public void deleteCategory() {
        System.out.println("\n===== 카테고리 삭제 =====");

        String categoryId = InputUtil.inputRequiredLine("삭제할 카테고리 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Category targetCategory = categoryRepository.findByCategoryId(con, categoryId);

            if (targetCategory == null) {
                System.out.println("카테고리가 없습니다.");
                return;
            }

            if (categoryRepository.hasChildren(con, targetCategory.getCategoryNo())) {
                System.out.println("- 하위 카테고리가 있어 삭제 불가 -");
                return;
            }

            categoryRepository.deleteByCategoryNo(con, targetCategory.getCategoryNo());

            con.commit();
            System.out.println("삭제 완료");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("카테고리 삭제 실패: " + e.getMessage());
            throw new RuntimeException("카테고리 삭제 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    private Integer selectParentCategoryNo(Connection con) throws Exception {
        List<Category> parentCategories = categoryRepository.findParents(con);

        if (parentCategories.isEmpty()) {
            System.out.println("먼저 대분류를 등록해야 합니다.");
            return null;
        }

        parentCategories.sort(Comparator.comparingInt(Category::getSortOrder));

        System.out.println("\n===== 상위 카테고리 선택 =====");
        for (int i = 0; i < parentCategories.size(); i++) {
            Category parentCategory = parentCategories.get(i);
            System.out.println((i + 1) + ". " +
                    parentCategory.getCategoryName() + " (" +
                    parentCategory.getCategoryId() + ", 정렬순서: " +
                    parentCategory.getSortOrder() + ")");
        }

        int choice = InputUtil.inputInt("번호 선택: ");

        if (choice < 1 || choice > parentCategories.size()) {
            System.out.println("잘못된 번호입니다.");
            return null;
        }

        return parentCategories.get(choice - 1).getCategoryNo();
    }
}
