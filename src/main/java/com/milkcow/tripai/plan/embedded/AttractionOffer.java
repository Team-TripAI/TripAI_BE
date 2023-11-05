package com.milkcow.tripai.plan.embedded;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttractionOffer{
    private String offerName;
    private Integer offerPrice;
    private String offerUrl;
}
