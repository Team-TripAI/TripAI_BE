package com.milkcow.tripai.plan.dto;

import com.milkcow.tripai.plan.embedded.AttractionData;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttractionDataDto {
    @ApiModelProperty(value = "조회된 명소 개수", example = "2")
    private int AttractionCount;
    private List<AttractionData> attractionDataList;
}
