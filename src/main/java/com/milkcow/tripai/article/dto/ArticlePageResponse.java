package com.milkcow.tripai.article.dto;

import com.milkcow.tripai.article.domain.Article;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class ArticlePageResponse {

    @ApiParam(value = "게시글 목록")
    @Schema(description = "조회된 게시글 목록")
    private final List<ArticleListResponse> articleList;

    @ApiParam(value = "총 페이지 수")
    @Schema(description = "총 페이지 수", example = "1")
    private final int totalPages;

    @ApiParam(value = "모든 페이지의 총 게시글 수")
    @Schema(description = "모든 페이지의 총 게시글 수", example = "1")
    private final long totalElements;

    @ApiParam(value = "현재 페이지가 첫 페이지인지 여부")
    @Schema(description = "현재 페이지가 첫 페이지인지 여부", example = "true")
    private final boolean first;

    @ApiParam(value = "현재 페이지가 마지막 페이지인지 여부")
    @Schema(description = "현재 페이지가 마지막 페이지인지 여부", example = "true")
    private final boolean last;

    @ApiParam(value = "현재 페이지 번호")
    @Schema(description = "현재 페이지 번호", example = "0")
    private final int number;

    @ApiParam(value = "페이지 당 조회될 게시글 수")
    @Schema(description = "페이지 당 조회될 게시글 수", example = "10")
    private final int size;

    @ApiParam(value = "실제로 조회된 게시글 수")
    @Schema(description = "실제로 조회된 게시글 수", example = "1")
    private final int numberOfElements;

    public static ArticlePageResponse from(Page<Article> articlePage) {
        return ArticlePageResponse.builder()
                .totalPages(articlePage.getTotalPages())
                .totalElements(articlePage.getTotalElements())
                .first(articlePage.isFirst())
                .last(articlePage.isLast())
                .number(articlePage.getNumber())
                .size(articlePage.getSize())
                .numberOfElements(articlePage.getNumberOfElements())
                .articleList(articlePage.getContent()
                        .stream()
                        .map(v -> ArticleListResponse.builder()
                                .articleId(v.getId())
                                .title(v.getTitle())
                                .locationName(v.getLocationName())
                                .formattedAddress(v.getFormattedAddress())
                                .image(v.getImage())
                                .nickname(v.getMember().getNickname())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    private static class ArticleListResponse {

        @ApiParam(value = "게시글 ID")
        @Schema(description = "조회된 게시글 ID", example = "1")
        private final Long articleId;

        @ApiParam(value = "게시글 제목")
        @Schema(description = "작성된 게시글 제목", example = "속초 배낚시")
        private final String title;

        @ApiParam(value = "여행장소 이름")
        @Schema(description = "여행장소 이름", example = "속초배낚시체험 모래기호")
        private final String locationName;

        @ApiParam(value = "여행장소 주소")
        @Schema(description = "여행장소 주소", example = "대한민국 강원도 속초시 장사항해안길 41")
        private final String formattedAddress;

        @ApiParam(value = "여행사진")
        @Schema(description = "여행사진", example = "해초1699197018587.png")
        private final String image;

        @ApiParam(value = "작성자 이름")
        @Schema(description = "작성자 이름", example = "박수영")
        private final String nickname;
    }
}
