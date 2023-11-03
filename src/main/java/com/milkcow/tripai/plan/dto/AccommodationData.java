package com.milkcow.tripai.plan.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationData {
    private String name;
    private Double lat;
    private Double lng;
    private LocalDate startTime;
    private LocalDate endTime;
    private Integer price;
    private String image;
}
