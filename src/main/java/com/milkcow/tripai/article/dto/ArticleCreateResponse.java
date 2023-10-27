package com.milkcow.tripai.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticleCreateResponse {

    private final Long articleId;

    public static ArticleCreateResponse from(Long articleId) {
        return new ArticleCreateResponse(articleId);
    }
}
