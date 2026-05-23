package kr.co.javaex.sec23;

import kr.co.javaex.sec23.controller.UserController;
import kr.co.javaex.sec23.service.UserService;
import kr.co.javaex.sec23.util.json.JsonUtil;

public class Ecommerce {

    public static void main(String[] args) {

        JsonUtil.readUsers("src/data/user.json");
        JsonUtil.readCategories("src/data/categories.json");
        JsonUtil.readProducts("src/data/products.json");
        JsonUtil.readProductCategoryMappings("src/data/product-category-mapping.json");
        JsonUtil.readCarts("src/data/carts.json");
        JsonUtil.readOrders("src/data/orders.json");

        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        userController.showGuestMenu();
    }
}