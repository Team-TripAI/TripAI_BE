package com.milkcow.tripai.plan.domain;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.plan.embedded.detailPlan.AccommodationPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.AttractionPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.FlightPlan;
import com.milkcow.tripai.plan.embedded.detailPlan.RestaurantPlan;
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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
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

}
