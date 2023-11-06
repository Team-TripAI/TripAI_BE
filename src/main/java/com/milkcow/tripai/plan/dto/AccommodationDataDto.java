package com.milkcow.tripai.plan.dto;

import com.milkcow.tripai.plan.embedded.AccommodationData;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationDataDto {
    @ApiModelProperty(value = "조회된 숙박 개수", example = "2")
    private Integer AccommodationCount;
    private List<AccommodationData> accommodationDataList;
}
