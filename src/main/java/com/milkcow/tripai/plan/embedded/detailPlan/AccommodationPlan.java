package com.milkcow.tripai.plan.embedded.detailPlan;

import java.time.LocalDate;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationPlan extends CommonDetailPlan{
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;
}
