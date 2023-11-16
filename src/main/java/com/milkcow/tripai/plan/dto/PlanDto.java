package com.milkcow.tripai.plan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.milkcow.tripai.plan.domain.Plan;
import com.milkcow.tripai.plan.embedded.detailPlan.AccommodationPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.AttractionPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.FlightPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.RestaurantPlan;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * 예산 기반 일정 DTO
 * request, response 동일
 */
@Data
@Builder
public class PlanDto {
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate end;
    private int total;
    private int accommodation;
    private int flight;
    private int restaurant;
    private int attraction;
    private List<AccommodationPlan> accommodationList;
    private List<FlightPlan> flightList;
    private List<RestaurantPlan> restaurantList;
    private List<AttractionPlan> attractionList;

    public static PlanDto from(Plan plan){
        return PlanDto.builder()
                .name(plan.getName())
                .start(plan.getStartDate())
                .end(plan.getEndDate())
                .total(plan.getTotalBudget())
                .accommodation(plan.getAccommodationBudget())
                .flight(plan.getFlightBudget())
                .restaurant(plan.getRestaurantBudget())
                .attraction(plan.getAttractionBudget())
                .accommodationList(plan.getAccommodationPlanList())
                .flightList(plan.getFlightPlanList())
                .restaurantList(plan.getRestaurantPlanList())
                .attractionList(plan.getAttractionPlanList())
                .build();
    }
}
