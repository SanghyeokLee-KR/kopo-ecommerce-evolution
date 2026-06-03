package kr.co.shop.service;

import jakarta.validation.constraints.Min;
import kr.co.shop.common.exception.BusinessException;
import kr.co.shop.common.exception.ErrorCode;
import kr.co.shop.domain.Basket;
import kr.co.shop.domain.BasketItem;
import kr.co.shop.domain.Product;
import kr.co.shop.domain.User;
import kr.co.shop.dto.basket.response.BasketItemResponse;
import kr.co.shop.dto.basket.response.BasketResponse;
import kr.co.shop.repository.jpa.BasketItemRepository;
import kr.co.shop.repository.jpa.BasketRepository;
import kr.co.shop.repository.jpa.ProductRepository;
import kr.co.shop.repository.jpa.UserRepository;
import kr.co.shop.repository.mapper.BasketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BasketMapper basketMapper;

    @Override
    @Transactional
    public void addItem(
            Long nbUser,
            Long nbProduct,
            @Min(value = 1, message = "수량은 1개 이상이어야 합니다.") int quantity
    ) {
        User user = userRepository.findById(nbUser).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(nbProduct).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        validateProductStock(product, quantity);

        // 장바구니가 없으면 최초 1회 생성
        Basket basket = basketRepository.findByUser_NbUser(nbUser).orElseGet(() -> basketRepository.save(Basket.builder().user(user).build()));

        BasketItem existing = basketItemRepository
                .findByBasket_NbBasketAndProduct_NbProduct(basket.getNbBasket(), nbProduct).orElse(null);

        if (existing != null) {
            int newQuantity = existing.getQtBasketItem() + quantity;
            validateProductStock(product, newQuantity);
            existing.increaseQuantity(quantity);

            log.info("장바구니 수량 증가 - nbUser={}, nbProduct={}, 변경 후 수량={}", nbUser, nbProduct, newQuantity);
            return;
        }

        // 순번은 현재 품목 수 + 1.
        // 삭제 후 순번이 비어도 정렬 기준으로만 쓰니까 갱신 X
        int nextOrder = (int) basketItemRepository.countByBasket_NbBasket(basket.getNbBasket()) + 1;

        basketItemRepository.save(
                BasketItem.builder()
                        .basket(basket)
                        .cnBasketItemOrder(nextOrder)
                        .product(product)
                        .qtBasketItemPrice(product.getQtSalePrice())
                        .qtBasketItem(quantity)
                        .qtBasketItemAmount(product.getQtSalePrice() * quantity)
                        .build()
        );

        log.info("장바구니 상품 추가 - nbUser={}, nbProduct={}, quantity={}", nbUser, nbProduct, quantity);
    }

    @Override
    public BasketResponse getBasket(Long nbUser) {
        Basket basket = basketRepository.findByUser_NbUser(nbUser).orElse(null);

        if (basket == null) {
            return new BasketResponse(null, List.of(), 0);
        }

        List<BasketItemResponse> items = basketMapper.findBasketItems(basket.getNbBasket());

        int totalAmount = items.stream()
                .mapToInt(BasketItemResponse::amount)
                .sum();

        return new BasketResponse(basket.getNbBasket(), items, totalAmount);
    }

    @Override
    @Transactional
    public void updateQuantity(
            Long nbUser,
            Long nbBasketItem,
            @Min(value = 1, message = "수량은 1개 이상이어야 합니다.") int quantity
    ) {
        BasketItem basketItem = basketItemRepository.findById(nbBasketItem)
                .orElseThrow(() -> new BusinessException(ErrorCode.BASKET_ITEM_NOT_FOUND));

        validateBasketOwner(nbUser, basketItem);
        validateProductStock(basketItem.getProduct(), quantity);

        basketItem.changeQuantity(quantity);

        log.info("장바구니 수량 변경 - nbUser={}, nbBasketItem={}, quantity={}", nbUser, nbBasketItem, quantity);
    }

    @Override
    @Transactional
    public void removeItem(Long nbUser, Long nbBasketItem) {
        BasketItem basketItem = basketItemRepository.findById(nbBasketItem)
                .orElseThrow(() -> new BusinessException(ErrorCode.BASKET_ITEM_NOT_FOUND));

        validateBasketOwner(nbUser, basketItem);
        basketItemRepository.delete(basketItem);

        log.info("장바구니 상품 삭제 - nbUser={}, nbBasketItem={}", nbUser, nbBasketItem);
    }

    @Override
    @Transactional
    public void clearBasket(Long nbUser) {
        basketRepository.findByUser_NbUser(nbUser).ifPresent(basket -> {
            basketItemRepository.deleteByBasket_NbBasket(basket.getNbBasket());
            log.info("장바구니 전체 비우기 완료 - nbUser={}, nbBasket={}", nbUser, basket.getNbBasket());
        });
    }

    private void validateProductStock(Product product, int quantity) {
        Integer stock = product.getQtStock();

        if (stock == null || stock <= 0) {
            log.warn("품절 상품 접근 - nbProduct={}, stock={}", product.getNbProduct(), stock);
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }

        if (stock < quantity) {
            log.warn("재고 부족 - nbProduct={}, 요청수량={}, 현재재고={}", product.getNbProduct(), quantity, stock);
            throw new BusinessException(ErrorCode.NOT_ENOUGH_STOCK);
        }
    }

    // 다른 사람 장바구니 아이템 건드리는 케이스 방어
    private void validateBasketOwner(Long nbUser, BasketItem basketItem) {
        Long ownerUserId = basketItem.getBasket().getUser().getNbUser();

        if (!ownerUserId.equals(nbUser)) {
            log.warn("장바구니 접근 권한 없음 - 요청자 nbUser={}, 소유자 nbUser={}, nbBasketItem={}",
                    nbUser, ownerUserId, basketItem.getNbBasketItem());
            throw new BusinessException(ErrorCode.FORBIDDEN_ORDER_ACCESS);
        }
    }
}