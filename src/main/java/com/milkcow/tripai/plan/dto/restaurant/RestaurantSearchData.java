package com.milkcow.tripai.plan.dto.restaurant;

import com.milkcow.tripai.plan.embedded.PlaceHour;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantSearchData {
    @ApiModelProperty(value = "맛집명", example = "Gyopao Gyoza Roppongi")
    private String name;
    @ApiModelProperty(value = "위도", example = "35.663578")
    private Double lat;
    @ApiModelProperty(value = "경도", example = "139.73212")
    private Double lng;
    @ApiModelProperty(value = "가격대", example = "$$ - $$$")
    private String price;
    private List<PlaceHour> hours;
    @ApiModelProperty(value = "이미지 url", example = "https://media-cdn.tripadvisor.com/media/photo-l/18/cc/b9/54/japanese-and-taiwan-fusion.jpg")
    private String image;
}
