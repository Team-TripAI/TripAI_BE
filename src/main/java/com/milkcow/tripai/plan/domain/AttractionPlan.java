package com.milkcow.tripai.plan.domain;

import com.milkcow.tripai.plan.embedded.PlaceHour;
import com.milkcow.tripai.plan.util.HoursConverter;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 명소 정보 저장 값타입
 */
@Entity
@Getter
@NoArgsConstructor
public class AttractionPlan extends CommonDetailPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Plan plan;

    @Convert(converter = HoursConverter.class)
    private List<PlaceHour> hours;
    @Column(length = 350)
    private String image;


    @Builder
    public AttractionPlan(@NotNull String name, double lat, double lng, List<PlaceHour> hours,
                          String image, Plan plan) {
        super(name, lat, lng);
        this.hours = hours;
        this.image = image;
        this.plan = plan;
    }
}
