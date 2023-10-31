package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.plan.dto.FlightDataDto;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

@SpringBootTest
class FlightPlanServiceNaverImplTest {
    @Autowired
    private FlightPlanServiceNaverImpl flightPlanServiceNaverImpl;
    
    @Test
    public void 항공권_조회() throws Exception{
        //given
        //when
        FlightDataDto flightData = flightPlanServiceNaverImpl.getFlightData("LAX", "ICN", "20231225", 2000000);
        //then
        System.out.println("flightData.getFlightDataList() = " + flightData.getFlightDataList());
        Assertions.assertThat(flightData.getFlightCount()).isPositive();
    }
    
}