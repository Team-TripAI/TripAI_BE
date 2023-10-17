package com.milkcow.tripai.plan.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class FlightDataDto {
    private int flightCount;
    private ArrayList<FlightData> flightDataList;
}
