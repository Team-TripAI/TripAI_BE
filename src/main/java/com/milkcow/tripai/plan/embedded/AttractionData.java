package com.milkcow.tripai.plan.embedded;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttractionData {
    @ApiModelProperty(value = "명소 명", example = "Meiji Jingu Shrine")
    private String name;
    @ApiModelProperty(value = "위도", example = "35.676167")
    private Double lat;
    @ApiModelProperty(value = "경도", example = "139.69952")
    private Double lng;
    @ApiModelProperty(value = "예약 가능 활동 개수", example = "2")
    private Integer offerCount;
    private List<AttractionOffer> offerList;
    @ApiModelProperty(value = "예약 가능 활동 가격대", example = "$$$")
    private String price;
    private List<PlaceHour> hours;
    @ApiModelProperty(value = "이미지 url", example = "https://media-cdn.tripadvisor.com/media/photo-l/11/a2/c7/9d/caption.jpg")
    private String image;
}
