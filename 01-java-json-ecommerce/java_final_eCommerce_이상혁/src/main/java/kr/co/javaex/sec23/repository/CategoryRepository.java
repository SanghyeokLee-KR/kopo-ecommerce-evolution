package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.Category;
import kr.co.javaex.sec23.util.json.JsonUtil;

import java.nio.file.Path;
import java.util.List;


/**
 * <h3>카테고리 레파지토리</h3>
 */
public class CategoryRepository {

    private final Path path = Path.of("src/data/categories.json");


    // 카테고리 json파일 전체 리스트로 가져오기
    public List<Category> findAll() {
        return JsonUtil.readCategories(path.toString());
    }

    // 카테고리 저장
    public void saveAll(List<Category> categories) {
        JsonUtil.writeCategories(path.toString(), categories);
    }

    // 카테고리 아이디 반환
    public Category findById(String id) {
        List<Category> categoryList = findAll();

        for (Category category : categoryList) {
            if (category.getCategoryId().equals(id)) {
                return category;
            }
        }

        return null;
    }
}