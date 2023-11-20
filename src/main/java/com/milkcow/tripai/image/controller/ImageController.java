package com.milkcow.tripai.image.controller;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.image.dto.ImageRequestDto;
import com.milkcow.tripai.image.dto.ImageResponseDto;
import com.milkcow.tripai.image.exception.ImageException;
import com.milkcow.tripai.image.result.ImageResult;
import com.milkcow.tripai.image.service.ImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    private static final String COLOR_PATTERN = "^([A-Fa-f0-9]{6})$";

    @PostMapping("/image")
    public DataResponse<ImageResponseDto> getSimilarPlace(@RequestBody ImageRequestDto imageRequestDto){
        List<String> colorList = imageRequestDto.getColorList();
        isValidHexColors(colorList);

        return imageService.searchSimilarPlace(imageRequestDto);
    }


    public void isValidHexColors(List<String> colorList) {
        for (String color : colorList) {
            if (!isValidHexColor(color)) {
                throw new ImageException(ImageResult.INVALID_COLOR_PATTERN);
            }
        }
    }

    private boolean isValidHexColor(String color) {
        return color.matches(COLOR_PATTERN);
    }
}
