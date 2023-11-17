package com.milkcow.tripai.plan.dto.accommodation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.milkcow.tripai.plan.domain.AccommodationPlan;
import com.milkcow.tripai.plan.domain.Plan;
import com.milkcow.tripai.plan.dto.CommonPlanDto;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AccommodationPlanDto extends CommonPlanDto {
    private String image;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public AccommodationPlanDto(String name, double lat, double lng, String image, LocalDate startDate,
                                LocalDate endDate) {
        super(name, lat, lng);
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static AccommodationPlanDto toDto(AccommodationPlan accommodationPlan) {
        return new AccommodationPlanDto(accommodationPlan.getName(), accommodationPlan.getLat(),
                accommodationPlan.getLng(), accommodationPlan.getImage(), accommodationPlan.getStartDate(),
                accommodationPlan.getEndDate());
    }

    public static AccommodationPlan toEntity(AccommodationPlanDto dto, Plan plan){
        return AccommodationPlan.builder()
                .plan(plan)
                .name(dto.getName())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .startDate(dto.getStartDate())
                .endDate((dto.getEndDate()))
                .image(dto.getImage())
                .build();
    }
}
