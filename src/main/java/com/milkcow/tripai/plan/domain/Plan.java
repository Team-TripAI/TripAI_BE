package com.milkcow.tripai.plan.domain;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.plan.dto.PlanDto;
import com.milkcow.tripai.plan.embedded.detailPlan.AccommodationPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.AttractionPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.FlightPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.RestaurantPlan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @ElementCollection
    @CollectionTable(name = "accommodationPlan",
            joinColumns = @JoinColumn(name = "plan_id"))
    private List<AccommodationPlan> accommodationPlanList = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "flightPlan",
            joinColumns = @JoinColumn(name = "plan_id"))
    private List<FlightPlan> flightPlanList = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "restaurantPlan",
            joinColumns = @JoinColumn(name = "plan_id"))
    private List<RestaurantPlan> restaurantPlanList = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "attractionPlan",
            joinColumns = @JoinColumn(name = "plan_id"))
    private List<AttractionPlan> attractionPlanList = new ArrayList<>();

    /**
     * 여행 계획 DTO를 엔티티로 변환
     * @param request {@link PlanDto} 여행 계획 DTO
     * @param member {@link Member} 여행 계획 등록 사용자
     * @return {@link Plan} Plan 엔티티
     */
    public static Plan of(PlanDto request, Member member){
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
                .accommodationPlanList(request.getAccommodationList())
                .flightPlanList(request.getFlightList())
                .restaurantPlanList(request.getRestaurantList())
                .attractionPlanList(request.getAttractionList())
                .build();
    }
}
