package com.milkcow.tripai.plan.dto.flight;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightSearchResponseDto {
    @ApiModelProperty(value = "조회된 항공권 개수", example = "2")
    private int flightCount;
    private List<FlightSearchData> flightSearchDataList;
}
