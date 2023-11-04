package com.milkcow.tripai.plan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationData {
    private String name;
    private Double lat;
    private Double lng;
    private String startDate;
    private String endDate;
    private Long price;
    private Long avgPrice;
    private String image;
}
