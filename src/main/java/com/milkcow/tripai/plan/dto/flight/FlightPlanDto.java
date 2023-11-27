package com.milkcow.tripai.plan.dto.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.milkcow.tripai.plan.domain.FlightPlan;
import com.milkcow.tripai.plan.domain.Plan;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class FlightPlanDto {
    @ApiModelProperty(value = "항공편명", example = "YP101")
    @NotNull
    private String name;

    @ApiModelProperty(value = "항공사명", example = "YP")
    @NotNull
    private String airline;

    @ApiModelProperty(value = "출발 공항", example = "ICN")
    @NotNull
    private String departureAirport;

    @ApiModelProperty(value = "도착 공항", example = "LAX")
    @NotNull
    private String arrivalAirport;

    @ApiModelProperty(value = "출발 시간", example = "2023-12-25T13:30")
    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "도착 시간", example = "2023-12-31T07:30")
    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "예약 url", example = "https://www.tripadvisor.com/CheapFlightsPartnerHandoff?searchHash=3a43bb45deca656b05417e46cead30e9&provider=SkyScanner|1|36&area=FLTCenterColumn|0|1|ItinList|2|Meta_ItineraryPrice&resultsServlet=CheapFlightsSearchResults&handoffPlatform=desktop&impressionId=&totalPricePerPassenger=873900.06")
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
