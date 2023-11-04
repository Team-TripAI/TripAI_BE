package com.milkcow.tripai.plan.service.restaurant;

import com.milkcow.tripai.plan.dto.RestaurantDataDto;

public interface RestaurantService {
    RestaurantDataDto getRestaurantData(String destination, String startDate, String endDate, int maxPrice);
}
