package com.milkcow.tripai.plan.dto.attraction;

import com.milkcow.tripai.plan.domain.AttractionPlan;
import com.milkcow.tripai.plan.dto.CommonPlanDto;
import com.milkcow.tripai.plan.embedded.PlaceHour;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttractionPlanDto extends CommonPlanDto {
    private List<PlaceHour> hours;
    private String image;

    public AttractionPlanDto(@NotNull String name, double lat, double lng, List<PlaceHour> hours,
                             String image) {
        super(name, lat, lng);
        this.hours = hours;
        this.image = image;
    }

    public static AttractionPlanDto toDto(AttractionPlan attractionPlan) {
        return new AttractionPlanDto(attractionPlan.getName(), attractionPlan.getLat(), attractionPlan.getLng(),
                attractionPlan.getHours(), attractionPlan.getImage());
    }
}
