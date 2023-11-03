package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.plan.dto.FlightDataDto;

public interface FlightPlanService {
    FlightDataDto getFlightData(String departureAirport, String arrivalAirport, String departureDate, int maxFare);
}
