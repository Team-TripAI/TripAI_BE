package com.milkcow.tripai.plan.dto.accommodation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationSearchData {
    @ApiModelProperty(value = "숙박지 명", example = "Lodging Tokyo Ueno")
    private String name;
    @ApiModelProperty(value = "위도", example = "35.7176401814438")
    private Double lat;
    @ApiModelProperty(value = "경도", example = "139.785188392868")
    private Double lng;
    @ApiModelProperty(value = "숙박 시작일", example = "2023-12-25")
    private String startDate;
    @ApiModelProperty(value = "숙박 종료일", example = "2023-12-31")
    private String endDate;
    @ApiModelProperty(value = "전체 가격", example = "141518")
    private Long price;
    @ApiModelProperty(value = "일 평균 가격", example = "23586")
    private Long avgPrice;
    @ApiModelProperty(value = "이미지 url", example = "https://cf.bstatic.com/xdata/images/hotel/square60/497650805.jpg?k=8a38c4d87eff3bf19478446616bf2349af721648120d685b67097d958b62ae01&o=")
    private String image;
}
