package com.milkcow.tripai.plan.dto;

import com.milkcow.tripai.plan.embedded.RestaurantData;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDataDto {
    @ApiModelProperty(value = "조회된 맛집 개수", example = "2")
    private int restaurantCount;
    private List<RestaurantData> restaurantDataList;
}
