package com.milkcow.tripai.image.dto;

import com.milkcow.tripai.image.domain.Image;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponseData {
    private String name;
    private String address;
    private double lat;
    private double lng;
    private String image;

    public static ImageResponseData toDto(Image image){
        return ImageResponseData.builder()
                .name(image.getLocationName())
                .address(image.getFormattedAddress())
                .lat(image.getLat())
                .lng(image.getLng())
                .image(image.getUuid())
                .build();
    }
}
