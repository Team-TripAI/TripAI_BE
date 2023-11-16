package com.milkcow.tripai.plan.embedded.detailPlan;

import java.time.LocalDate;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 숙박 정보 저장 값타입
 */
@Embeddable
@Getter
@NoArgsConstructor
public class AccommodationPlan extends CommonDetailPlan{
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public AccommodationPlan(@NotNull String name, double lat, double lng, String image,
                             LocalDate startDate, LocalDate endDate) {
        super(name, lat, lng);
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
