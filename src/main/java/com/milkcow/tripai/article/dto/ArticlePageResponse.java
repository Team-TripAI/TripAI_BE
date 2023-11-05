package com.milkcow.tripai.article.dto;

import com.milkcow.tripai.article.domain.Article;
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

    private final List<ArticleListResponse> articleList;

    private final int totalPages;

    private final long totalElements;

    private final boolean first;

    private final boolean last;

    private final int number;

    private final int size;

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
        private final Long articleId;

        private final String title;

        private final String locationName;

        private final String formattedAddress;

        private final String image;

        private final String nickname;
    }
}
