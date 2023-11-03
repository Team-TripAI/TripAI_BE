package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.plan.dto.FlightDataDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FlightPlanServiceTest {

    @Autowired
    private FlightPlanService flightPlanServiceAPI;

    @Test
    public void 항공권_조회() throws Exception{
        //given
        //when
        FlightDataDto flightData = flightPlanServiceAPI.getFlightData("GMP", "CJU", "2023-12-25", 987654321);
        //then
        System.out.println("flightData.getFlightDataList() = " + flightData.getFlightDataList());
        System.out.println("flightData.getFlightCount() = " + flightData.getFlightCount());
        Assertions.assertThat(flightData.getFlightCount()).isPositive();
    }

}