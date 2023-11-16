package com.milkcow.tripai.plan.embedded.detailPlan;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 숙박, 맛집, 명소 공통 정보 저장 추상 클래스
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CommonDetailPlan {
    @NotNull
    private String name;
    private double lat;
    private double lng;
}
