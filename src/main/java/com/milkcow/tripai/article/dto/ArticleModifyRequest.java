package com.milkcow.tripai.article.dto;

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
public class ArticleModifyRequest {
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
}
