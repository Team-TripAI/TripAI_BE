package com.milkcow.tripai.plan.service;

import static org.junit.jupiter.api.Assertions.*;

import com.milkcow.tripai.plan.dto.AttractionDataDto;
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
        AttractionDataDto attractionData = attractionService.getAttractionData(destination, maxPrice);
        //then
        Assertions.assertThat(attractionData.getAttractionCount()).isPositive();
        System.out.println("attractionData.getAttractionDataList() = " + attractionData.getAttractionDataList());
    }
}