package com.milkcow.tripai.plan.embedded.detailPlan;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Embeddable
@Getter
public abstract class CommonDetailPlan {
    @NotNull
    private String name;
    private double lat;
    private double lng;
}
