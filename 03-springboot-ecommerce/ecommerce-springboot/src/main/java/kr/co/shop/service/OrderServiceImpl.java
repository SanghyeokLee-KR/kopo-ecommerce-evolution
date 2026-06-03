package kr.co.shop.service;

import kr.co.shop.common.enums.OrderStatus;
import kr.co.shop.common.exception.BusinessException;
import kr.co.shop.common.exception.ErrorCode;
import kr.co.shop.domain.Basket;
import kr.co.shop.domain.BasketItem;
import kr.co.shop.domain.Order;
import kr.co.shop.domain.OrderItem;
import kr.co.shop.domain.Product;
import kr.co.shop.domain.User;
import kr.co.shop.dto.order.response.OrderCompleteResponse;
import kr.co.shop.dto.order.response.OrderDetailResponse;
import kr.co.shop.dto.order.response.OrderItemResponse;
import kr.co.shop.dto.order.response.OrderListResponse;
import kr.co.shop.repository.jpa.*;
import kr.co.shop.repository.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    @Retryable(
            retryFor = {TransientDataAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public OrderCompleteResponse orderFromBasket(Long nbUser, List<Long> nbBasketItemIds) {
        // 먼저 유저 및 장바구니 조회하고
        User user = userRepository.findById(nbUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Basket basket = basketRepository.findByUser_NbUser(nbUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.BASKET_NOT_FOUND));

        List<BasketItem> basketItems = basketItemRepository.findByBasket_NbBasketOrderByCnBasketItemOrderAsc(basket.getNbBasket());

        // 선택 된거 선택된 장바구니 아이템 필터링
        if (nbBasketItemIds != null && !nbBasketItemIds.isEmpty()) {
            Set<Long> selectedItemIds = new HashSet<>(nbBasketItemIds);
            basketItems = basketItems.stream()
                    .filter(item -> selectedItemIds.contains(item.getNbBasketItem()))
                    .toList();
        }

        if (basketItems.isEmpty()) {
            log.warn("장바구니 주문 실패 - 유효한 장바구니 아이템이 없음: nbUser={}", nbUser);
            throw new BusinessException(ErrorCode.BASKET_EMPTY);
        }

        //  주문 생성
        int orderAmount = basketItems.stream().mapToInt(BasketItem::getQtBasketItemAmount).sum();
        Order order = Order.builder()
                .user(user)
                .qtOrderAmount(orderAmount)
                .nmOrderPerson(user.getNmUser())
                .nmDeliveryAddress("기본 배송지")
                .daOrder(LocalDateTime.now())
                .stOrder(OrderStatus.ORDER_COMPLETE.getCode())
                .build();
        orderRepository.save(order);

        // 주문 상세랑, 생성 및 재고 차감
        List<OrderItem> orderItemsToSave = new ArrayList<>();
        int orderItemOrder = 1;

        for (BasketItem basketItem : basketItems) {
            Product product = basketItem.getProduct();
            product.decreaseStock(basketItem.getQtBasketItem());

            orderItemsToSave.add(OrderItem.builder()
                    .order(order)
                    .cnOrderItem(orderItemOrder++)
                    .product(product)
                    .user(user)
                    .qtUnitPrice(basketItem.getQtBasketItemPrice())
                    .qtOrderItem(basketItem.getQtBasketItem())
                    .build());
        }

        orderItemRepository.saveAll(orderItemsToSave);

        // 5. 장바구니 비우기
        if (nbBasketItemIds != null && !nbBasketItemIds.isEmpty()) {
            basketItemRepository.deleteAllInBatch(basketItems); // 성능을 위해 InBatch 고려
        } else {
            basketItemRepository.deleteByBasket_NbBasket(basket.getNbBasket());
        }

        // 반영
        orderItemRepository.flush();

        // 응 답 결과 세팅 아
        List<OrderItemResponse> orderItemResponses = orderMapper.findMyOrderItems(order.getNbOrder());

        log.info("장바구니 주문 완료 - nbUser={}, nbOrder={}, orderAmount={}", nbUser, order.getNbOrder(), order.getQtOrderAmount());

        return new OrderCompleteResponse(
                order.getNbOrder(),
                order.getNmOrderPerson(),
                order.getDaOrder().format(DATE_TIME_FORMATTER),
                order.getStOrder(),
                order.getQtOrderAmount(),
                orderItemResponses
        );
    }

    @Override
    @Transactional
    public OrderCompleteResponse orderDirect(Long nbUser, Long nbProduct, int quantity) {
        // 수량 0 이하 들어오면 1개로 보정. 컨트롤러 수정 예정
        if (quantity <= 0) {
            log.warn("바로 주문 수량 비정상 - nbUser={}, nbProduct={}, quantity={} → 1로 보정", nbUser, nbProduct, quantity);
            quantity = 1;
        }

        User user = userRepository.findById(nbUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(nbProduct)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        product.decreaseStock(quantity);

        int orderAmount = product.getQtSalePrice() * quantity;

        // TODO: 하드코딩 수정 예정
        Order order = Order.builder()
                .user(user)
                .qtOrderAmount(orderAmount)
                .nmOrderPerson(user.getNmUser())
                .nmDeliveryAddress("기본 배송지")
                .daOrder(LocalDateTime.now())
                .stOrder(OrderStatus.ORDER_COMPLETE.getCode())
                .build();

        orderRepository.save(order);

        orderItemRepository.save(
                OrderItem.builder()
                        .order(order)
                        .cnOrderItem(1)
                        .product(product)
                        .user(user)
                        .qtUnitPrice(product.getQtSalePrice())
                        .qtOrderItem(quantity)
                        .build()
        );

        List<OrderItemResponse> items = orderMapper.findMyOrderItems(order.getNbOrder());

        log.info("바로 주문 완료 - nbUser={}, nbOrder={}, nbProduct={}, quantity={}, orderAmount={}",
                nbUser, order.getNbOrder(), nbProduct, quantity, orderAmount);

        return new OrderCompleteResponse(
                order.getNbOrder(),
                order.getNmOrderPerson(),
                order.getDaOrder().format(DATE_TIME_FORMATTER),
                order.getStOrder(),
                order.getQtOrderAmount(),
                items
        );
    }

    @Override
    public List<OrderListResponse> findMyOrders(Long nbUser) {
        return orderMapper.findMyOrders(nbUser);
    }

    @Override
    public OrderDetailResponse findMyOrderDetail(Long nbUser, Long nbOrder) {
        // nbUser 조건을 같이 넘겨서 다른 사람 주문 조회 못 하게 막음
        OrderDetailResponse order = orderMapper.findMyOrderDetail(nbUser, nbOrder);

        if (order == null) {
            log.warn("주문 상세 조회 실패 - 존재하지 않거나 접근 권한 없음: nbUser={}, nbOrder={}", nbUser, nbOrder);
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        List<OrderItemResponse> items = orderMapper.findMyOrderItems(nbOrder);

        return new OrderDetailResponse(
                order.nbOrder(),
                order.nmOrderPerson(),
                order.nmDeliveryAddress(),
                order.daOrder(),
                order.stOrder(),
                order.orderAmount(),
                items
        );
    }
}