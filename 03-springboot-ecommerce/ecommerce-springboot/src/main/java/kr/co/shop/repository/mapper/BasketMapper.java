package kr.co.shop.repository.mapper;

import kr.co.shop.dto.basket.response.BasketItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 장바구니 조회 Mapper
 */
@Mapper
public interface BasketMapper {

    // 장바구니 화면 품목 조회
    List<BasketItemResponse> findBasketItems(
            @Param("nbBasket") Long nbBasket
    );
}