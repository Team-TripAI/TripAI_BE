package com.milkcow.tripai.plan.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationDataDto {
    private Integer AccommodationCount;
    private List<AccommodationData> accommodationDataList;
}
