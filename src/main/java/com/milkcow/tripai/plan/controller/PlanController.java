package com.milkcow.tripai.plan.controller;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.plan.dto.AccommodationDataDto;
import com.milkcow.tripai.plan.dto.FlightDataDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanResult;
import com.milkcow.tripai.plan.service.accommodation.AccommodationService;
import com.milkcow.tripai.plan.service.flight.FlightService;
import com.milkcow.tripai.plan.util.DateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan/budget")
public class PlanController {

    private final FlightService flightService;
    private final AccommodationService accommodationService;

    @GetMapping("/flight")
    public DataResponse<FlightDataDto> getFlightPlan(@RequestParam String departureAirport,
                                                     @RequestParam String arrivalAirport,
                                                     @RequestParam String departure,
                                                     @RequestParam(defaultValue = "0x7fffffff") int maxFare) {
        if (!DateValidator.isValidDate(departure)) {
            throw new PlanException(PlanResult.INVALID_DATE);
        }
        FlightDataDto flightData = flightService.getFlightData(departureAirport, arrivalAirport, departure,
                maxFare);
        return DataResponse.create(flightData, PlanResult.OK_FLIGHT_PLAN);
    }

    @GetMapping("/accommodation")
    public DataResponse<AccommodationDataDto> getAccommodationPlan(@RequestParam String destination,
                                                                   @RequestParam String startDate,
                                                                   @RequestParam String endDate,
                                                                   @RequestParam(defaultValue = "0x7fffffff") int maxPrice) {
        if (!DateValidator.isValidDate(startDate) || !DateValidator.isValidDate(endDate)) {
            throw new PlanException(PlanResult.INVALID_DATE);
        }
        AccommodationDataDto accommodationData = accommodationService.getAccommodationData(destination, startDate,
                endDate, maxPrice);
        return DataResponse.create(accommodationData, PlanResult.OK_ACCOMMODATION_PLAN);
    }
}
