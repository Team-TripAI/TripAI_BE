package com.milkcow.tripai.article.dto;

import com.milkcow.tripai.article.domain.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class ArticleDetailResponse {

    private final Long articleId;

    private final String title;

    private final String content;

    private final String locationName;

    private final String formattedAddress;

    private final String image;

    private final String nickname;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    public static ArticleDetailResponse from(Article article) {
        return ArticleDetailResponse.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .locationName(article.getLocationName())
                .formattedAddress(article.getFormattedAddress())
                .image(article.getImage())
                .nickname(article.getMember().getNickname())
                .createDate(article.getCreateDate())
                .modifyDate(article.getModifyDate())
                .build();
    }
}
