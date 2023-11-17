package com.milkcow.tripai.plan.dto.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.milkcow.tripai.plan.domain.FlightPlan;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FlightPlanDto {
    private String name;
    private String airline;
    private String departureAirport;
    private String arrivalAirport;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    private String url;

    public static FlightPlanDto toDto(FlightPlan flightPlan){
        return new FlightPlanDto(flightPlan.getName(),
                flightPlan.getAirline(),
                flightPlan.getDepartureAirport(),
                flightPlan.getArrivalAirport(),
                flightPlan.getStartTime(),
                flightPlan.getEndTime(),
                flightPlan.getUrl());
    }
}
