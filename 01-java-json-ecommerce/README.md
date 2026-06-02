# 파일 기반 e-Commerce (콘솔 쇼핑몰)

KOPO 2026 자바 최종과제로 만든 콘솔 쇼핑몰이다. JSON 파일을 DB처럼 사용하고, 로그인한 계정의 권한(관리자 / 일반회원)에 따라 메뉴가 나뉜다.

과제 조건에 "코드 개발에 ChatGPT 등 LLM을 참조하지 말 것"이 명시돼 있어서, 구조 설계부터 로직까지 전부 직접 작성했다. 마감 전날 거의 밤을 새워서 끝냈다.

<img src="docs/assignment/page-01.png" width="640" alt="표지">

## 실행 방법

- 실행 클래스: `Ecommerce`
- 관리자 로그인은 이메일 `admin`, 비밀번호 `admin`.
- 일반회원은 회원가입 후 관리자가 승인해야 로그인된다. 가입 직후 상태는 `가입요청`이다.
- 실행하면 `src/data/` 아래 JSON 파일들을 읽어온다.

JDK 21 / Gradle 9 환경에서 `gradlew build`로 빌드된다.

## 사용 기술

Java 21, Gradle, Jackson(`ObjectMapper`)을 썼다. 별도 DB 없이 JSON 파일 6개로 데이터를 저장하고, 객체와 JSON 사이 변환은 전부 Jackson으로 처리했다.

## 폴더 구조

과제에서 지정한 패키지 구조를 그대로 따랐다.

```
kr.co.javaex.sec23
├─ Ecommerce.java     실행 진입점
├─ controller         콘솔 메뉴 / 입력 처리
├─ service            비즈니스 로직
├─ repository         JSON 파일 읽기·쓰기 (DB 역할)
├─ domain             데이터 클래스(POJO)
└─ util               JsonUtil(Jackson), InputUtil, enum 상태값
```

## 기능

관리자

- 카테고리: 등록 / 수정 / 삭제, 대분류·중분류(자기참조), 정렬 순서
- 상품: 등록 / 수정 / 삭제 / 조회, 재고 수정, 판매중지, 재고가 0이면 자동 품절
- 상품-카테고리 매핑: 등록 / 수정 / 삭제 / 조회
- 회원 관리: 전체 조회, 가입 승인, 권한 변경, 탈퇴 처리

일반회원

- 회원: 가입(형식 검증) / 로그인 / 로그아웃 / 정보 수정 / 비밀번호 변경 / 탈퇴 요청
- 상품 보기: 전체, 카테고리별, 가격순, 이름 검색, 상세
- 장바구니: 담기 / 조회 / 수량 변경 / 삭제 / 비우기
- 주문: 장바구니 전체·선택 주문, 바로 주문, 내역·상세 조회, 취소(재고 복구)

## 구현하면서 신경 쓴 점

- 콘솔이라 컨트롤러를 새로 만들면 로그인 상태가 사라진다. 그래서 `UserService`를 `main`에서 한 번만 만들어 컨트롤러에 넘겨주는 방식으로 로그인 정보를 유지했다.
- 주문하면 재고를 깎고, 취소하면 되돌린다. 재고가 0이 되면 자동으로 품절로 바꾼다. 주문 처리 중에는 검증을 모두 통과한 다음 한 번에 파일로 저장하도록 했다.
- 회원과 상품 상태는 enum으로 관리했다. 회원은 `가입요청 → 정상 → 탈퇴요청 → 탈퇴완료`, 상품은 `정상 / 판매중지 / 품절`.
- 회원 ID와 비밀번호는 정규식으로 형식을 검사하고, 숫자·양수·필수 입력 같은 처리는 `InputUtil`에 모아 뒀다.

## 데이터 파일

- `user.json` : 회원
- `categories.json` : 카테고리(대·중분류)
- `products.json` : 상품
- `product-category-mapping.json` : 상품-카테고리 매핑
- `carts.json` : 장바구니
- `orders.json` : 주문

## 앞으로 (2단계)

지금은 JSON 파일에 저장하지만, 다음에는 JDBC로 실제 DB를 붙이고 최종적으로 Spring Boot로 옮길 생각이다. 과제에서 지정한 `controller / service / repository` 구조가 스프링과 거의 같아서 옮기는 부담은 크지 않을 것 같다. 자세한 건 학교 시험과 과제를 끝낸 뒤에 정리할 예정이다.

<details>
<summary>과제 원본 (PDF 19페이지)</summary>

**02. 작업 범위 — 관리 기능**
![작업 범위 관리 기능](docs/assignment/page-02.png)

**03. 작업 범위 — 사용자 기능**
![작업 범위 사용자 기능](docs/assignment/page-03.png)

**04. 과제 제출 안내 (LLM 사용 금지)**
![과제 제출 안내](docs/assignment/page-04.png)

**05. 카테고리 관리 기능**
![카테고리 관리 기능](docs/assignment/page-05.png)

**06. 상품 관리 기능**
![상품 관리 기능](docs/assignment/page-06.png)

**07. 상품 관리 자료구조**
![상품 관리 자료구조](docs/assignment/page-07.png)

**08. 상품-카테고리 매핑**
![상품 카테고리 매핑](docs/assignment/page-08.png)

**09. 회원 관리 기능**
![회원 관리 기능](docs/assignment/page-09.png)

**10. 회원 관리 로그인·자료구조**
![회원 관리 로그인](docs/assignment/page-10.png)

**11. 상품 전시 기능**
![상품 전시 기능](docs/assignment/page-11.png)

**12. 장바구니 기능**
![장바구니 기능](docs/assignment/page-12.png)

**13. 장바구니 자료구조**
![장바구니 자료구조](docs/assignment/page-13.png)

**14. 주문 기능**
![주문 기능](docs/assignment/page-14.png)

**15. 관리자 추가 기능**
![관리자 추가 기능](docs/assignment/page-15.png)

**16. 패키지 구조 설명**
![패키지 구조 설명](docs/assignment/page-16.png)

**17. 사용 클래스 설명 (ObjectMapper)**
![ObjectMapper 설명](docs/assignment/page-17.png)

**18. build.gradle 의존성**
![build.gradle 의존성](docs/assignment/page-18.png)

**19. ObjectMapper 사용법**
![ObjectMapper 사용법](docs/assignment/page-19.png)

</details>
