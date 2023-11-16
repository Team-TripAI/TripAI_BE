package com.milkcow.tripai.plan.embedded.detailPlan;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 항공권 정보 저장 값타입
 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightPlan{
    @NotNull
    private String name;
    @NotNull
    private String airline;
    @NotNull
    private String departure;
    @NotNull
    private String arrival;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    @Column(length = 350)
    private String url;
}
