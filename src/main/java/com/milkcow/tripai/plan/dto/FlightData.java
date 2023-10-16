package com.milkcow.tripai.plan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightData {
    private String id;
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String airline;
    private String departureTime;
    private String arrivalTime;
    private int fare;

    @Override
    public String toString() {
        return "FlightData{" +
                "id='" + id + '\'' +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", airline='" + airline + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", fare=" + fare +
                '}';
    }
}
