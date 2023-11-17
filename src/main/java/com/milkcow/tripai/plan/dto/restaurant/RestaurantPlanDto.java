package com.milkcow.tripai.plan.dto.restaurant;

import com.milkcow.tripai.plan.domain.RestaurantPlan;
import com.milkcow.tripai.plan.dto.CommonPlanDto;
import com.milkcow.tripai.plan.embedded.PlaceHour;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestaurantPlanDto extends CommonPlanDto {
    private List<PlaceHour> hours;
    private String image;

    public RestaurantPlanDto(@NotNull String name, double lat, double lng, List<PlaceHour> hours,
                             String image) {
        super(name, lat, lng);
        this.hours = hours;
        this.image = image;
    }

    public static RestaurantPlanDto toDto(RestaurantPlan restaurantPlan) {
        return new RestaurantPlanDto(restaurantPlan.getName(), restaurantPlan.getLat(), restaurantPlan.getLng(),
                restaurantPlan.getHours(), restaurantPlan.getImage());
    }
}
