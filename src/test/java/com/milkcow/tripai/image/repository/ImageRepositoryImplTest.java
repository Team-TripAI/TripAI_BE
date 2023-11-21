package com.milkcow.tripai.image.repository;

import com.milkcow.tripai.global.config.TestQueryDslConfig;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.dto.ImageRequestDto;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class ImageRepositoryImplTest {
    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setDB(){
        Image image1 = getImage("Seoul", "a", "b", "c", "d", "e");
        Image image2 = getImage("Pangyo", "b", "c", "d", "e", "f");
        Image image3 = getImage("Los Angeles", "c", "d", "e", "f", "g");
        Image image4 = getImage("Las Vegas", "d", "e", "f", "g", "h");
        Image image5 = getImage("Osaka", "e", "h", "i", "j", "k");

        imageRepository.save(image1);
        imageRepository.save(image2);
        imageRepository.save(image3);
        imageRepository.save(image4);
        imageRepository.save(image5);
    }

    @Test
    public void 조건에_맞는_이미지_검색_결과_없음() throws Exception{
        //given
        ImageRequestDto requestDto = ImageRequestDto.builder()
                .labelList(List.of("city", "skyline", "mountain", "building", "downtown"))
                .colorList(List.of("123456", "123456", "123456", "123456", "123456"))
                .build();
        //when
        List<Image> images = imageRepository.searchSimilar(requestDto.getLabelList(),
                requestDto.stringToColor());
        //then
        Assertions.assertThat(images.isEmpty()).isTrue();
    }

    @Test
    public void 조건에_맞는_이미지_검색_1() throws Exception{
        //given
        ImageRequestDto requestDto = ImageRequestDto.builder()
                .labelList(List.of("a", "city", "building", "mountain", "downtown"))
                .colorList(List.of("123456", "123456", "123456", "123456", "123456"))
                .build();
        //when
        labelSearchTest(requestDto, 1);
    }

    @Test
    public void 조건에_맞는_이미지_검색_2() throws Exception{
        //given
        ImageRequestDto requestDto = ImageRequestDto.builder()
                .labelList(List.of("a", "b", "building", "mountain", "downtown"))
                .colorList(List.of("123456", "123456", "123456", "123456", "123456"))
                .build();
        //when
        labelSearchTest(requestDto, 2);
    }

    @Test
    public void 조건에_맞는_이미지_검색_3() throws Exception{
        //given
        ImageRequestDto requestDto = ImageRequestDto.builder()
                .labelList(List.of("a", "b", "c", "mountain", "downtown"))
                .colorList(List.of("123456", "123456", "123456", "123456", "123456"))
                .build();
        //when
        labelSearchTest(requestDto, 3);
    }

    @Test
    public void 조건에_맞는_이미지_검색_4() throws Exception{
        //given
        ImageRequestDto requestDto = ImageRequestDto.builder()
                .labelList(List.of("a", "b", "c", "d", "downtown"))
                .colorList(List.of("123456", "123456", "123456", "123456", "123456"))
                .build();
        //when
        labelSearchTest(requestDto, 4);
    }

    @Test
    public void 조건에_맞는_이미지_검색_5() throws Exception{
        //given
        ImageRequestDto requestDto = ImageRequestDto.builder()
                .labelList(List.of("a", "b", "c", "d", "e"))
                .colorList(List.of("123456", "123456", "123456", "123456", "123456"))
                .build();
        labelSearchTest(requestDto, 5);
    }

    private void labelSearchTest(ImageRequestDto requestDto, int expected) {
        //when
        List<Image> images = imageRepository.searchSimilar(requestDto.getLabelList(),
                requestDto.stringToColor());
        //then
        Assertions.assertThat(images.size()).isEqualTo(expected);
    }

    private static Image getImage(String name, String l1, String l2, String l3, String l4, String l5) {
        return Image.builder()
                .locationName(name)
                .formattedAddress("seoul, Korea")
                .lat(35.0)
                .lng(139.0)
                .label1(l1)
                .label2(l2)
                .label3(l3)
                .label4(l4)
                .label5(l5)
                .color1("121212")
                .color2("232323")
                .color3("343434")
                .color4("454545")
                .color5("565656")
                .uuid("123")
                .build();
    }

}