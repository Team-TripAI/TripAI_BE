package com.milkcow.tripai.plan.controller;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.plan.dto.FlightDataDto;
import com.milkcow.tripai.plan.result.PlanResult;
import com.milkcow.tripai.plan.service.FlightPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlanController {

    private final FlightPlanService flightPlanService;

    @GetMapping("/plan/budget/flight")
    public DataResponse<FlightDataDto> getFlightPlan(@RequestParam String departureAirport,
                                                     @RequestParam String arrivalAirport,
                                                     @RequestParam String departure,
                                                     @RequestParam int maxFare){
        FlightDataDto flightData = flightPlanService.getFlightData(departureAirport, arrivalAirport, departure, maxFare);
        return DataResponse.create(flightData, PlanResult.OK_FLIGHT_PLAN);
    }
}
