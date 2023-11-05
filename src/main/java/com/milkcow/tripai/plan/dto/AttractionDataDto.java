package com.milkcow.tripai.plan.dto;

import com.milkcow.tripai.plan.embedded.AttractionData;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttractionDataDto {
    private int AttractionCount;
    private List<AttractionData> attractionDataList;
}
