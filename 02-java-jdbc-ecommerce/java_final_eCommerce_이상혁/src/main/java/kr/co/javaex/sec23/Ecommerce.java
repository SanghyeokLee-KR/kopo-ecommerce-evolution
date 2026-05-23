package kr.co.javaex.sec23;

import kr.co.javaex.sec23.controller.UserController;
import kr.co.javaex.sec23.service.UserService;

public class Ecommerce {

    public static void main(String[] args) {

        // https://www.notion.so/JSON-JDBC-Refactoring-Project-3461e4d919d58041aa88d19849f29217?showMoveTo=true 읽어주세요 ㅠㅠ
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        userController.showGuestMenu();
    }
}