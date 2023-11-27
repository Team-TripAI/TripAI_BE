package com.milkcow.tripai.plan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class CommonPlanDto {
    @ApiModelProperty(value = "이름", example = "오사카")
    private String name;
    @ApiModelProperty(value = "위도", example = "35.7176401814438")
    private double lat;
    @ApiModelProperty(value = "경도", example = "139.785188392868")
    private double lng;

    public CommonPlanDto(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
}
