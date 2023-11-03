package com.milkcow.tripai.plan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttractionData {
    private String name;
    private Double lat;
    private Double lng;
    private String price;
    private String start;
    private String end;
    private String image;
}
