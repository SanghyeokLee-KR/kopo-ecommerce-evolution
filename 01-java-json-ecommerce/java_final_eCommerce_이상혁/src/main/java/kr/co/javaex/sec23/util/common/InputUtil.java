package kr.co.javaex.sec23.util.common;

import java.util.Scanner;

/**
 * 사용자 입력 처리 유틸 클래스
 */
public class InputUtil {

    private static final Scanner SCANNER = new Scanner(System.in);

    // 문자열 입력
    public static String inputLine(String message) {
        System.out.print(message);
        return SCANNER.nextLine();
    }

    // 공백 방지 문자
    public static String inputRequiredLine(String message) {
        while (true) {
            System.out.print(message);
            String input = SCANNER.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("필수 입력 항목입니다.");
        }
    }

    // 정수 입력
    public static int inputInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(SCANNER.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력하세요.");
            }
        }
    }

    // 0 이상
    public static int inputNonNegativeInt(String message) {
        while (true) {
            int value = inputInt(message);

            if (value >= 0) {
                return value;
            }

            System.out.println("0 이상만 입력 가능합니다.");
        }
    }

    // 1이상
    public static int inputPositiveInt(String message) {
        while (true) {
            int value = inputInt(message);

            if (value > 0) {
                return value;
            }

            System.out.println("1 이상만 입력 가능합니다.");
        }
    }
}