package com.milkcow.tripai.plan.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonPlanDto {
    private String name;
    private double lat;
    private double lng;

    public CommonPlanDto(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
}
