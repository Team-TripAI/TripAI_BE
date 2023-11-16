package com.milkcow.tripai.plan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
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
 * 숙박 정보 저장 값타입
 */
@Entity
@Getter
@NoArgsConstructor
public class AccommodationPlan extends CommonDetailPlan{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Plan plan;

    private String image;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public AccommodationPlan setPlan(Plan plan) {
        this.plan = plan;
        return this;
    }

    @Builder
    public AccommodationPlan(@NotNull String name, double lat, double lng, String image,
                             LocalDate startDate, LocalDate endDate) {
        super(name, lat, lng);
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
