package com.milkcow.tripai.plan.embedded;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightData {
    private String id;
    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
    private String airline;
    private String departureTime;
    private String arrivalTime;
    private Integer fare;
    private String url;

    @Override
    public String toString() {
        return "FlightData{" +
                "id='" + id + '\'' +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", airline='" + airline + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", fare=" + fare +
                ", url='" + url + '\'' +
                '}';
    }
}
