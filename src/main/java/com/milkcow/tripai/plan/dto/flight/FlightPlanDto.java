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

    public FlightPlanDto(String name, String airline, String departureAirport, String arrivalAirport,
                         LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static FlightPlanDto toDto(FlightPlan flightPlan) {
        return new FlightPlanDto(flightPlan.getName(),
                flightPlan.getAirline(),
                flightPlan.getDepartureAirport(),
                flightPlan.getArrivalAirport(),
                flightPlan.getStartTime(),
                flightPlan.getEndTime());
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
                .build();
    }
}
