package com.milkcow.tripai.plan.embedded.detailPlan;

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
public class AttractionPlan extends CommonDetailPlan{
    private String hours;
    private String image;
}
