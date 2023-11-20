package com.milkcow.tripai.article.dto;

import com.milkcow.tripai.image.domain.Image;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Schema
public class ArticleCreateRequest {

    @NotBlank
    @Size(max = 20)
    @ApiParam(value = "게시글 제목")
    @ApiModelProperty(example = "속초 배낚시")
    private final String title;

    @NotNull
    @Size(max = 200)
    @ApiParam(value = "게시글 내용")
    @ApiModelProperty(example = "해초 전문 낚시꾼 박지안")
    private final String content;

    @NotNull
    @ApiParam(value = "여행장소 이름")
    @ApiModelProperty(example = "속초배낚시체험 모래기호")
    private final String locationName;

    @NotNull
    @ApiParam(value = "여행장소 주소")
    @ApiModelProperty(example = "대한민국 강원도 속초시 장사항해안길 41")
    private final String formattedAddress;

    @NotNull
    @ApiParam(value = "여행사진")
    @ApiModelProperty(example = "해초1699197018587.png")
    private final String image;

    @NotNull
    @ApiParam(value = "여행장소 위도")
    @ApiModelProperty(example = "38.2252707")
    private final Double lat;

    @NotNull
    @ApiParam(value = "여행장소 경도")
    @ApiModelProperty(example = "128.5883593")
    private final Double lng;

    @NotNull
    @Size(min = 5, max = 5)
    @ApiParam(value = "여행사진 내의 레이블 목록")
    @ApiModelProperty(example = "[\n" +
            "\t\"Water\", \"Sky\", \"Cloud\", \"Boats and boating--Equipment and supplies\", \"Travel\"\n" +
            "]")
    private final List<String> labelList;

    @NotNull
    @Size(min = 5, max = 5)
    @ApiParam(value = "여행사진 내의 주요 색상 목록")
    @ApiModelProperty(example = "[\n" +
            "\t\"819CBA\", \"759BCC\", \"69809B\", \"A4C2E2\", \"405771\"\n" +
            "]")
    private final List<String> colorList;

    public final Image toImage() {
        assert labelList != null;
        assert colorList != null;

        return Image.builder()
                .uuid(image)
                .lat(lat)
                .lng(lng)
                .locationName(locationName)
                .formattedAddress(formattedAddress)
                .label1(labelList.get(0))
                .label2(labelList.get(1))
                .label3(labelList.get(2))
                .label4(labelList.get(3))
                .label5(labelList.get(4))
                .color1(colorList.get(0))
                .color2(colorList.get(1))
                .color3(colorList.get(2))
                .color4(colorList.get(3))
                .color5(colorList.get(4))
                .build();
    }
}
