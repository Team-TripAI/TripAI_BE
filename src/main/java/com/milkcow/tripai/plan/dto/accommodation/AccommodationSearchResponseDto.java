package com.milkcow.tripai.plan.dto.accommodation;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationSearchResponseDto {
    @ApiModelProperty(value = "조회된 숙박 개수", example = "2")
    private Integer AccommodationCount;
    private List<AccommodationSearchData> accommodationSearchDataList;
}
