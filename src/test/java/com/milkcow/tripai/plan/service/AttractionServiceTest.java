package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.plan.dto.attraction.AttractionSearchResponseDto;
import com.milkcow.tripai.plan.service.attraction.AttractionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AttractionServiceTest {
    @Autowired
    private AttractionService attractionService;

    @Test
    public void 명소_조회() throws Exception {
        //given
        String destination = "tokyo";
        int maxPrice = 1000000;
        //when
        AttractionSearchResponseDto attractionData = attractionService.getAttractionData(destination, maxPrice);
        //then
        Assertions.assertThat(attractionData.getAttractionCount()).isPositive();
        System.out.println("attractionData.getAttractionDataList() = " + attractionData.getAttractionSearchDataList());
    }
}