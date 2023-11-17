package com.milkcow.tripai.plan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.milkcow.tripai.plan.dto.accommodation.AccommodationPlanDto;
import com.milkcow.tripai.plan.dto.attraction.AttractionPlanDto;
import com.milkcow.tripai.plan.dto.flight.FlightPlanDto;
import com.milkcow.tripai.plan.dto.restaurant.RestaurantPlanDto;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class PlanRequestDto {
    @ApiModelProperty(value = "여행지", example = "도쿄")
    private final String name;

    @ApiModelProperty(value = "여행 시작일", example = "2023-12-25")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate start;

    @ApiModelProperty(value = "여행 종료일", example = "2023-12-31")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate end;

    @ApiModelProperty(value = "전체 예산", example = "100")
    private final Integer total;

    @ApiModelProperty(value = "숙소 예산", example = "30")
    private final Integer accommodation;

    @ApiModelProperty(value = "항공권 예산", example = "30")
    private final Integer flight;

    @ApiModelProperty(value = "맛집 예산", example = "30")
    private final Integer restaurant;

    @ApiModelProperty(value = "명소 예산", example = "10")
    private final Integer attraction;

    private final List<AccommodationPlanDto> accommodationList;
    private final List<FlightPlanDto> flightList;
    private final List<RestaurantPlanDto> restaurantList;
    private final List<AttractionPlanDto> attractionList;
}
