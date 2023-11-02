package com.milkcow.tripai.article.dto;

import com.milkcow.tripai.article.domain.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private final List<CommentDetailResponse> commentList;

    public static ArticleDetailResponse of(Article article, List<CommentSearch> searchedComments) {
        List<CommentDetailResponse> commentList = searchedComments.stream()
                .map(CommentDetailResponse::from)
                .collect(Collectors.toList());

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
                .commentList(commentList)
                .build();
    }
}
