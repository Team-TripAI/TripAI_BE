package com.milkcow.tripai.plan.service.flight;

import com.milkcow.tripai.plan.dto.FlightDataDto;

public interface FlightService {
    FlightDataDto getFlightData(String departureAirport, String arrivalAirport, String departureDate, int maxFare);
}
