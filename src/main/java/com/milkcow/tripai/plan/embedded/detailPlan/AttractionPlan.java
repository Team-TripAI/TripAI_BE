package com.milkcow.tripai.plan.embedded.detailPlan;

import com.milkcow.tripai.plan.embedded.PlaceHour;
import com.milkcow.tripai.plan.util.HoursConverter;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 명소 정보 저장 값타입
 */
@Embeddable
@Getter
@NoArgsConstructor
public class AttractionPlan extends CommonDetailPlan{
    @Convert(converter = HoursConverter.class)
    private List<PlaceHour> hours;
    @Column(length = 350)
    private String image;

    @Builder
    public AttractionPlan(@NotNull String name, double lat, double lng, List<PlaceHour> hours,
                          String image) {
        super(name, lat, lng);
        this.hours = hours;
        this.image = image;
    }
}
