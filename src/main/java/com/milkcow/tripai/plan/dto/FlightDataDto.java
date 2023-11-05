package com.milkcow.tripai.plan.dto;

import com.milkcow.tripai.plan.embedded.FlightData;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightDataDto {
    private int flightCount;
    private List<FlightData> flightDataList;
}
