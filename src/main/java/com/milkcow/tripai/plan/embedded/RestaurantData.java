package com.milkcow.tripai.plan.embedded;

import com.milkcow.tripai.plan.embedded.PlaceHour;
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
    private List<PlaceHour> hours;
    private String image;
}
