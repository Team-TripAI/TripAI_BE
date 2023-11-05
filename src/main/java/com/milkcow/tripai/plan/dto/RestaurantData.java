package com.milkcow.tripai.plan.dto;

import com.milkcow.tripai.plan.embedded.RestaurantHour;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantData {
    private String name;
    private Double lat;
    private Double lng;
    private String price;
    private List<RestaurantHour> hours;
    private String image;
}
