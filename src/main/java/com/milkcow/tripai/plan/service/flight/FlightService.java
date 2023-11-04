package com.milkcow.tripai.plan.service.flight;

import com.milkcow.tripai.plan.dto.FlightDataDto;

/**
 * 항공권 정보를 가져오는 service
 */
public interface FlightService {
    /**
     * 항공권 정보를 가져오는 메서드
     * @param departureAirport 출발 공항 명(IATA 코드)
     * @param arrivalAirport 도착 공항 명(IATA 코드)
     * @param departureDate 출발 일자(yyyy-MM-dd 형식)명
     * @param maxFare 최대 항공비
     * @return {@link FlightDataDto}
     */
    FlightDataDto getFlightData(String departureAirport, String arrivalAirport, String departureDate, int maxFare);
}
