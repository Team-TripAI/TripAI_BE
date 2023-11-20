package com.milkcow.tripai.image.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponseDto {
    private int recommendCount;
    private List<ImageResponseData> recommendList;
}
