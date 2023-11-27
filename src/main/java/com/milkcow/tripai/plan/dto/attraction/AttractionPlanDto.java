package com.milkcow.tripai.plan.dto.attraction;

import com.milkcow.tripai.plan.domain.AttractionPlan;
import com.milkcow.tripai.plan.domain.Plan;
import com.milkcow.tripai.plan.dto.CommonPlanDto;
import com.milkcow.tripai.plan.embedded.PlaceHour;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AttractionPlanDto extends CommonPlanDto {
    @Size(max = 7)
    private List<PlaceHour> hours;
    @ApiModelProperty(value ="명소 사진", example = "400")
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

    public static AttractionPlan toEntity(AttractionPlanDto dto, Plan plan) {
        return AttractionPlan.builder()
                .plan(plan)
                .name(dto.getName())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .hours(dto.hours)
                .image(dto.image)
                .build();
    }
}
