package com.milkcow.tripai.article.dto;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.image.domain.Image;
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
public class ArticleCreateRequest {

    @NotBlank
    @Size(max = 20)
    private final String title;

    @NotNull
    @Size(max = 200)
    private final String content;

    @NotNull
    private final String locationName;

    @NotNull
    private final String formattedAddress;

    @NotNull
    private final String image;

    @NotNull
    private final Double lat;

    @NotNull
    private final Double lng;

    @NotNull
    private final List<String> labelList;

    @NotNull
    private final List<String> colorList;

    public final Article toArticle() {
        return Article.builder()
                .title(title)
                .content(content)
                .locationName(locationName)
                .formattedAddress(formattedAddress)
                .image(image)
                .build();
    }

    public final Image toImage() {
        assert labelList != null;
        assert colorList != null;

        return Image.builder()
                .image(image)
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
