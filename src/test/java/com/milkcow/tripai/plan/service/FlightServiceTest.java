package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.plan.dto.flight.FlightSearchResponseDto;
import com.milkcow.tripai.plan.service.flight.FlightService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FlightServiceTest {

    @Autowired
    private FlightService flightServiceAPI;

    @Test
    public void 항공권_조회() throws Exception{
        //given
        //when
        FlightSearchResponseDto flightData = flightServiceAPI.getFlightData("GMP", "CJU", "2023-12-25", 987654321);
        //then
        System.out.println("flightData.getFlightDataList() = " + flightData.getFlightSearchDataList());
        System.out.println("flightData.getFlightCount() = " + flightData.getFlightCount());
        Assertions.assertThat(flightData.getFlightCount()).isPositive();
    }

}