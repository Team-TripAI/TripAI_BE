package com.milkcow.tripai.plan.embedded.detailPlan;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightPlan extends CommonDetailPlan{
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
    private String url;
}
