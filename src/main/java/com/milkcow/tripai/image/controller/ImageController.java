package com.milkcow.tripai.image.controller;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.image.dto.ImageRequestDto;
import com.milkcow.tripai.image.dto.ImageResponseDto;
import com.milkcow.tripai.image.exception.ImageException;
import com.milkcow.tripai.image.result.ImageResult;
import com.milkcow.tripai.image.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "ImageController", description = "이미지 기반 장소 추천 API")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    private static final String COLOR_PATTERN = "^([A-Fa-f0-9]{6})$";

    /**
     * 이미지 기반 장소 추천
     *
     * @param imageRequestDto {@link ImageRequestDto}
     * @return {@link ImageResponseDto}
     */
    @ApiOperation(value = "이미지 기반 장소 추천", notes = "유사한 사진을 통해 여행지를 추천한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imageRequestDto", value = "요청할 이미지 정보", readOnly = true, paramType = "body", dataTypeClass = ImageRequestDto.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "이미지 기반 추천 성공"),
            @ApiResponse(code = 400, message = "유효하지 않은 색상값"),
            @ApiResponse(code = 404, message = "유사한 이미지 없음"),
            @ApiResponse(code = 500, message = "서버 내 요류"),
    })
    @PostMapping("/image")
    public DataResponse<ImageResponseDto> getSimilarPlace(@RequestBody ImageRequestDto imageRequestDto) {
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
