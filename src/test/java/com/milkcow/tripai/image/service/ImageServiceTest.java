package com.milkcow.tripai.image.service;


import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.image.dto.ImageRequestDto;
import com.milkcow.tripai.image.dto.ImageResponseDto;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Test
    public void 이미지_기반_추천() throws Exception{
        //given
        List<String> labelList = List.of("Water", "Sky", "Cloud", "Boats and boating--Equipment and supplies", "Travel");
        List<String> colorList = List.of("819CBA", "759BCC", "69809B", "A4C2E2", "405771");
        ImageRequestDto requestDto = ImageRequestDto.builder().labelList(labelList).colorList(colorList).build();
        //when
        DataResponse<ImageResponseDto> dataResponse = imageService.searchSimilarPlace(requestDto);
        //then
        int recommendCount = dataResponse.getData().getRecommendCount();
        Assertions.assertThat(recommendCount).isPositive();
        System.out.println("dataResponse = " + dataResponse.getData());
    }
}