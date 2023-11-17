package com.milkcow.tripai.plan.dto.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.milkcow.tripai.plan.domain.FlightPlan;
import com.milkcow.tripai.plan.domain.Plan;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
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

    public FlightPlanDto(String name, String airline, String departureAirport, String arrivalAirport,
                         LocalDateTime startTime, LocalDateTime endTime, String url) {
        this.name = name;
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.startTime = startTime;
        this.endTime = endTime;
        this.url = url;
    }

    public static FlightPlanDto toDto(FlightPlan flightPlan) {
        return new FlightPlanDto(flightPlan.getName(),
                flightPlan.getAirline(),
                flightPlan.getDepartureAirport(),
                flightPlan.getArrivalAirport(),
                flightPlan.getStartTime(),
                flightPlan.getEndTime(),
                flightPlan.getUrl());
    }

    public static FlightPlan toEntity(FlightPlanDto dto, Plan plan) {
        return FlightPlan.builder()
                .plan(plan)
                .name(dto.name)
                .airline(dto.airline)
                .departureAirport(dto.departureAirport)
                .arrivalAirport(dto.arrivalAirport)
                .startTime(dto.startTime)
                .endTime(dto.endTime)
                .url(dto.url)
                .build();
    }
}
