package com.milkcow.tripai.plan.embedded;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class AttractionData {
    private String name;
    private Double lat;
    private Double lng;
    private Integer offerCount;
    private List<AttractionOffer> offerList;
    private String price;
    private List<PlaceHour> hours;
    private String image;
}
