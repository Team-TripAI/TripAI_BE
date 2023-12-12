package com.milkcow.tripai.image.service;


import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.dto.ImageRequestDto;
import com.milkcow.tripai.image.dto.ImageResponseDto;
import com.milkcow.tripai.image.repository.ImageRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageServiceTest {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setImageRepository(){
        List<Image> imageList = getImageList();
        imageRepository.saveAll(imageList);
    }


    @Test
    public void 이미지_기반_추천() throws Exception{
        //given
        //해운대 사
        List<String> labelList = List.of("Water", "Cloud", "Sky", "Building", "World");
        List<String> colorList = List.of("93CAE9", "1D79B3", "F4DDA5", "0E1514", "273716");
        ImageRequestDto requestDto = ImageRequestDto.builder().labelList(labelList).colorList(colorList).build();
        //when
        DataResponse<ImageResponseDto> dataResponse = imageService.searchSimilarPlace(requestDto);
        //then
        int recommendCount = dataResponse.getData().getRecommendCount();
        Assertions.assertThat(recommendCount).isPositive();
        System.out.println("dataResponse = " + dataResponse.getData());
    }

    private List<Image> getImageList() {
        Image image1 = Image.builder()
                .locationName("강릉 안목 해수욕장")
                .formattedAddress("강릉, 대한민국")
                .lat(37.494496)
                .lng(126.959932)
                .label1("Cloud")
                .label2("Water")
                .label3("Sky")
                .label4("Blue")
                .label5("Fluid")
                .color1("65A9BB")
                .color2("6FADE6")
                .color3("84ACA9")
                .color4("77B8EC")
                .color5("9BAE93")
                .uuid("1")
                .build();
        Image image2 = Image.builder()
                .locationName("말리부 주마 비치")
                .formattedAddress("말리부, 미국")
                .lat(37.494496)
                .lng(126.959932)
                .label1("Water")
                .label2("Sky")
                .label3("Atmosphere")
                .label4("Afterglow")
                .label5("Beach")
                .color1("312C27")
                .color2("FFCD54")
                .color3("221E1A")
                .color4("574F49")
                .color5("413224")
                .uuid("2")
                .build();
        Image image3 = Image.builder()
                .locationName("그랜드 캐니언")
                .formattedAddress("네바다, 미국")
                .lat(37.494496)
                .lng(126.959932)
                .label1("Cloud")
                .label2("Sky")
                .label3("Natural Landscape")
                .label4("Mountain")
                .label5("Bedrock")
                .color1("D2DFE6")
                .color2("ABCBE1")
                .color3("A9977B")
                .color4("281316")
                .color5("9D989D")
                .uuid("3")
                .build();
        Image image4 = Image.builder()
                .locationName("Banff National Park")
                .formattedAddress("캘거리, 캐나다")
                .lat(37.494496)
                .lng(126.959932)
                .label1("Water")
                .label2("Sky")
                .label3("Mountain")
                .label4("Snow")
                .label5("Natural Landscape")
                .color1("203352")
                .color2("333622")
                .color3("B3C3E5")
                .color4("2F5B56")
                .color5("1B345E")
                .uuid("4")
                .build();
        Image image5 = Image.builder()
                .locationName("을왕리 해수욕장")
                .formattedAddress("인천, 대한민국")
                .lat(37.494496)
                .lng(126.959932)
                .label1("Sky")
                .label2("Water")
                .label3("Beach")
                .label4("Coastal And Oceanic Landforms")
                .label5("Horizon")
                .color1("B0C2D1")
                .color2("8F9BA0")
                .color3("32393D")
                .color4("AA9A81")
                .color5("7C807F")
                .uuid("5")
                .build();
        Image image6 = Image.builder()
                .locationName("로지네 강아지 해변")
                .formattedAddress("롱비치, 미국")
                .lat(37.494496)
                .lng(126.959932)
                .label1("Water")
                .label2("Sky")
                .label3("Parachute")
                .label4("Azure")
                .label5("Natural Environment")
                .color1("689DCF")
                .color2("86A9CA")
                .color3("4984C1")
                .color4("5E7F9C")
                .color5("315E7F")
                .uuid("6")
                .build();
        Image image7 = Image.builder()
                .locationName("사하라 사막")
                .formattedAddress("이집트")
                .lat(37.494496)
                .lng(126.959932)
                .label1("Sky")
                .label2("Natural Environment")
                .label3("Slope")
                .label4("Landscape")
                .label5("Erg")
                .color1("853E1C")
                .color2("D77A3B")
                .color3("52271A")
                .color4("A9A5A5")
                .color5("C0937A")
                .uuid("7")
                .build();
        Image image8 = Image.builder()
                .locationName("베니스 비치")
                .formattedAddress("LA, 미국")
                .lat(37.494496)
                .lng(126.959932)
                .label1("Water")
                .label2("Cloud")
                .label3("Sky")
                .label4("Water Resources")
                .label5("Lake")
                .color1("A8C9EC")
                .color2("5B798C")
                .color3("9A9C99")
                .color4("97BEEB")
                .color5("84A9D9")
                .uuid("8")
                .build();

        return List.of(image1, image2, image3, image4, image5, image6, image7, image8);
    }
}