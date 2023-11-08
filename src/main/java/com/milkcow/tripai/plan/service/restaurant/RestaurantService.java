package com.milkcow.tripai.plan.service.restaurant;

import com.milkcow.tripai.plan.dto.restaurant.RestaurantSearchResponseDto;

/**
 * 맛집 정보를 가져오는 service
 */
public interface RestaurantService {
    /**
     * 맛집 정보를 가져오는 메서드
     * @param destination 목적지(영어만 가능, 3글자 이상)
     * @param startDate 여행 시작일(yyyy-MM-dd 형식)
     * @param endDate 여행 종료일(yyyy-MM-dd 형식)
     * @param maxPrice 최대 식비
     * @return {@link RestaurantSearchResponseDto}
     */
    RestaurantSearchResponseDto getRestaurantData(String destination, String startDate, String endDate, int maxPrice);
}
