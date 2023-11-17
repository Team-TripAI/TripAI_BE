package com.milkcow.tripai.plan.domain;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.plan.dto.PlanRequestDto;
import com.milkcow.tripai.plan.dto.accommodation.AccommodationPlanDto;
import com.milkcow.tripai.plan.dto.attraction.AttractionPlanDto;
import com.milkcow.tripai.plan.dto.flight.FlightPlanDto;
import com.milkcow.tripai.plan.dto.restaurant.RestaurantPlanDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예산기반 일정 엔티티
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Plan {

    @Id
    @GeneratedValue
    @Column(name = "plan_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createDateTime;
    private String name;
    private int totalBudget;
    private int accommodationBudget;
    private int flightBudget;
    private int restaurantBudget;
    private int attractionBudget;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AccommodationPlan> accommodationPlanList = new ArrayList<>();
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FlightPlan> flightPlanList = new ArrayList<>();
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RestaurantPlan> restaurantPlanList = new ArrayList<>();
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AttractionPlan> attractionPlanList = new ArrayList<>();

    private void setDetailPlanList(List<AccommodationPlan> accommodationPlanList,
                                   List<FlightPlan> flightPlanList,
                                   List<RestaurantPlan> restaurantPlanList,
                                   List<AttractionPlan> attractionPlanList) {
        this.accommodationPlanList = accommodationPlanList;
        this.flightPlanList = flightPlanList;
        this.restaurantPlanList = restaurantPlanList;
        this.attractionPlanList = attractionPlanList;
    }

    /**
     * 여행 계획 DTO를 엔티티로 변환
     *
     * @param request {@link PlanRequestDto} 여행 계획 DTO
     * @param member  {@link Member} 여행 계획 등록 사용자
     * @return {@link Plan} Plan 엔티티
     */
    public static Plan of(PlanRequestDto request, Member member) {
        Plan plan = defaultPlanBuild(request, member);

        parseDtoToEntity(request, plan);
        return plan;
    }

    private static void parseDtoToEntity(PlanRequestDto request, Plan plan) {
        List<AccommodationPlanDto> accommodationList = request.getAccommodationList();
        List<FlightPlanDto> flightList = request.getFlightList();
        List<RestaurantPlanDto> restaurantList = request.getRestaurantList();
        List<AttractionPlanDto> attractionList = request.getAttractionList();

        List<AccommodationPlan> accommodationPlans = accommodationList.stream().map(a -> AccommodationPlanDto.toEntity(a,
                        plan))
                .collect(Collectors.toList());

        List<FlightPlan> flightPlans = flightList.stream().map(a -> FlightPlanDto.toEntity(a, plan))
                .collect(Collectors.toList());
        List<RestaurantPlan> restaurantPlans = restaurantList.stream().map(a -> RestaurantPlanDto.toEntity(a, plan))
                .collect(Collectors.toList());
        List<AttractionPlan> attractionPlans = attractionList.stream().map(a -> AttractionPlanDto.toEntity(a, plan))
                .collect(Collectors.toList());

        plan.setDetailPlanList(accommodationPlans, flightPlans, restaurantPlans, attractionPlans);
    }

    private static Plan defaultPlanBuild(PlanRequestDto request, Member member) {
        return Plan.builder()
                .member(member)
                .startDate(request.getStart())
                .endDate(request.getEnd())
                .createDateTime(LocalDateTime.now())
                .name(request.getName())
                .totalBudget(request.getTotal())
                .accommodationBudget(request.getAccommodation())
                .flightBudget(request.getFlight())
                .restaurantBudget(request.getRestaurant())
                .attractionBudget(request.getAttraction())
                .build();
    }
}
