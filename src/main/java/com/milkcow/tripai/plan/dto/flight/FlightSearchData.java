package com.milkcow.tripai.plan.dto.flight;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightSearchData {
    @ApiModelProperty(value = "항공권 id", example = "YP101")
    private String id;
    @ApiModelProperty(value = "출발 공항", example = "ICN")
    private String departureAirport;
    @ApiModelProperty(value = "도착 공항", example = "LAX")
    private String arrivalAirport;
    @ApiModelProperty(value = "출발 일자", example = "2023-12-25")
    private String departureDate;
    @ApiModelProperty(value = "도착 일자", example = "2023-12-25")
    private String arrivalDate;
    @ApiModelProperty(value = "항공사 id", example = "YP")
    private String airline;
    @ApiModelProperty(value = "출발 시간", example = "13:30:00+09:00")
    private String departureTime;
    @ApiModelProperty(value = "도착 시간", example = "07:20:00-08:00")
    private String arrivalTime;
    @ApiModelProperty(value = "가격", example = "873900")
    private Integer fare;

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
                '}';
    }
}
