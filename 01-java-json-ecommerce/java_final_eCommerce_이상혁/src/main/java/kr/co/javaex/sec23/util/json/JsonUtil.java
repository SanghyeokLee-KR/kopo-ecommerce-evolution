package kr.co.javaex.sec23.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.javaex.sec23.domain.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JSON 파일 읽기 저장 유틸 클래스
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    // 유저
    public static List<User> readUsers(String path) {

        try {
            Path userPath = Path.of(path);

            if (!Files.exists(userPath)) {
                Files.createDirectories(userPath.getParent());
                Files.writeString(userPath, "[]", StandardOpenOption.CREATE);
                return new ArrayList<>();
            }

            String userJson = Files.readString(userPath).trim();

            if (userJson.isEmpty()) {
                return new ArrayList<>();
            }

            User[] arrUser = mapper.readValue(userJson, User[].class);
            return new ArrayList<>(Arrays.asList(arrUser));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("user.json 읽기 실패");
        }
    }

    public static void writeUsers(String path, List<User> users) {
        try {
            Path userPath = Path.of(path);
            Files.createDirectories(userPath.getParent());

            String json = mapper.writeValueAsString(users);

            Files.writeString(
                    userPath,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("user.json 저장 실패");
        }
    }

    // 카테고리
    public static List<Category> readCategories(String path) {
        try {
            Path categoryPath = Path.of(path);

            if (!Files.exists(categoryPath)) {
                Files.createDirectories(categoryPath.getParent());
                Files.writeString(categoryPath, "[]", StandardOpenOption.CREATE);
                return new ArrayList<>();
            }

            String categoryJson = Files.readString(categoryPath).trim();

            if (categoryJson.isEmpty()) {
                return new ArrayList<>();
            }

            Category[] arrCategory = mapper.readValue(categoryJson, Category[].class);
            return new ArrayList<>(Arrays.asList(arrCategory));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("category.json 읽기 실패");
        }
    }

    public static void writeCategories(String path, List<Category> categories) {
        try {
            Path categoryPath = Path.of(path);
            Files.createDirectories(categoryPath.getParent());

            String json = mapper.writeValueAsString(categories);

            Files.writeString(
                    categoryPath,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("category.json 저장 실패");
        }
    }

    // 상품관리
    public static List<Product> readProducts(String path) {
        try {
            Path productPath = Path.of(path);

            if (!Files.exists(productPath)) {
                Files.createDirectories(productPath.getParent());
                Files.writeString(productPath, "[]", StandardOpenOption.CREATE);
                return new ArrayList<>();
            }

            String productJson = Files.readString(productPath).trim();

            if (productJson.isEmpty()) {
                return new ArrayList<>();
            }

            Product[] arrProduct = mapper.readValue(productJson, Product[].class);
            return new ArrayList<>(Arrays.asList(arrProduct));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("products.json 읽기 실패");
        }
    }

    public static void writeProducts(String path, List<Product> products) {
        try {
            Path productPath = Path.of(path);
            Files.createDirectories(productPath.getParent());

            String json = mapper.writeValueAsString(products);

            Files.writeString(
                    productPath,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("products.json 저장 실패");
        }
    }

    // 카테고리 상품 맵핑
    public static List<ProductCategoryMapping> readProductCategoryMappings(String path) {
        try {
            Path mappingPath = Path.of(path);

            if (!Files.exists(mappingPath)) {
                Files.createDirectories(mappingPath.getParent());
                Files.writeString(mappingPath, "[]", StandardOpenOption.CREATE);
                return new ArrayList<>();
            }

            String mappingJson = Files.readString(mappingPath).trim();

            if (mappingJson.isEmpty()) {
                return new ArrayList<>();
            }

            ProductCategoryMapping[] arrMapping =
                    mapper.readValue(mappingJson, ProductCategoryMapping[].class);

            return new ArrayList<>(Arrays.asList(arrMapping));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("product-category-mapping.json 읽기 실패");
        }
    }

    public static void writeProductCategoryMappings(String path, List<ProductCategoryMapping> mappingList) {
        try {
            Path mappingPath = Path.of(path);
            Files.createDirectories(mappingPath.getParent());

            String json = mapper.writeValueAsString(mappingList);

            Files.writeString(
                    mappingPath,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("product-category-mapping.json 저장 실패");
        }
    }

    //  카트
    public static List<Cart> readCarts(String path) {
        try {
            Path cartPath = Path.of(path);

            if (!Files.exists(cartPath)) {
                Files.createDirectories(cartPath.getParent());
                Files.writeString(cartPath, "[]", StandardOpenOption.CREATE);
                return new ArrayList<>();
            }

            String cartJson = Files.readString(cartPath).trim();

            if (cartJson.isEmpty()) {
                return new ArrayList<>();
            }

            Cart[] arrCart = mapper.readValue(cartJson, Cart[].class);
            return new ArrayList<>(Arrays.asList(arrCart));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("carts.json 읽기 실패");
        }
    }

    public static void writeCarts(String path, List<Cart> cartList) {
        try {
            Path cartPath = Path.of(path);
            Files.createDirectories(cartPath.getParent());

            String json = mapper.writeValueAsString(cartList);

            Files.writeString(
                    cartPath,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("carts.json 저장 실패");
        }
    }


    // 주문
    public static List<Order> readOrders(String path) {
        try {
            Path orderPath = Path.of(path);

            if (!Files.exists(orderPath)) {
                Files.createDirectories(orderPath.getParent());
                Files.writeString(orderPath, "[]", StandardOpenOption.CREATE);
                return new ArrayList<>();
            }

            String orderJson = Files.readString(orderPath).trim();

            if (orderJson.isEmpty()) {
                return new ArrayList<>();
            }

            Order[] arrOrder = mapper.readValue(orderJson, Order[].class);
            return new ArrayList<>(Arrays.asList(arrOrder));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("orders.json 읽기 실패");
        }
    }

    public static void writeOrders(String path, List<Order> orderList) {
        try {
            Path orderPath = Path.of(path);
            Files.createDirectories(orderPath.getParent());

            String json = mapper.writeValueAsString(orderList);

            Files.writeString(
                    orderPath,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("orders.json 저장 실패");
        }
    }
}