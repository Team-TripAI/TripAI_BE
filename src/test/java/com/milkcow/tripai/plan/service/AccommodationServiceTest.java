package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.plan.dto.accommodation.AccommodationSearchResponseDto;
import com.milkcow.tripai.plan.service.accommodation.AccommodationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccommodationServiceTest {
    @Autowired
    private AccommodationService accommodationService;

    @Test
    public void 숙박_조회() throws Exception{
        //given
        String destination = "도쿄";
        String startDate = "2023-12-25";
        String endDate = "2023-12-31";
        int maxPrice = 200000;
        //when
        AccommodationSearchResponseDto accommodationData = accommodationService.getAccommodationData(destination, startDate,
                endDate, maxPrice);
        //then
        System.out.println("accommodationCount = " + accommodationData.getAccommodationCount());
        System.out.println("accommodationList = " + accommodationData.getAccommodationSearchDataList());
        Assertions.assertThat(accommodationData.getAccommodationCount()).isPositive();
    }
}