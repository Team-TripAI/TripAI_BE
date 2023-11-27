package com.milkcow.tripai.image.dto;

import com.milkcow.tripai.image.service.ImageScore;
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
    private double score;

    public static ImageResponseData toDto(ImageScore imageScore) {
        return ImageResponseData.builder()
                .name(imageScore.getImage().getLocationName())
                .address(imageScore.getImage().getFormattedAddress())
                .lat(imageScore.getImage().getLat())
                .lng(imageScore.getImage().getLng())
                .image(imageScore.getImage().getUuid())
                .score(imageScore.getScore())
                .build();
    }
}
