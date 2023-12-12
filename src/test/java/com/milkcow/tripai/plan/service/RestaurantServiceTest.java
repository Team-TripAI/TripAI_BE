package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.plan.dto.restaurant.RestaurantSearchResponseDto;
import com.milkcow.tripai.plan.service.restaurant.RestaurantService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestaurantServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void 맛집_조회() throws Exception {
        //given
        String destination = "tokyo";
        String startDate = "2023-12-25";
        String endDate = "2023-12-31";
        int maxPrice = 1000000;
        //when
        RestaurantSearchResponseDto restaurantData = restaurantService.getRestaurantData(destination, startDate, endDate,
                maxPrice);
        //then
        Assertions.assertThat(restaurantData.getRestaurantCount()).isPositive();
        System.out.println("restaurantData.getRestaurantDataList() = " + restaurantData.getRestaurantSearchDataList());
    }

}