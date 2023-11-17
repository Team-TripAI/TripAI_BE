package com.milkcow.tripai.plan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 항공권 정보 저장 값타입
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Plan plan;

    @NotNull
    private String name;
    @NotNull
    private String airline;
    @NotNull
    private String departureAirport;
    @NotNull
    private String arrivalAirport;
    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;
    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    @NotNull
    @Column(length = 350)
    private String url;
}
