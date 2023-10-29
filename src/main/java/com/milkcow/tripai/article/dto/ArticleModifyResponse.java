package com.milkcow.tripai.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticleModifyResponse {

    private final Long articleId;

    public static ArticleModifyResponse from(Long articleId) {
        return new ArticleModifyResponse(articleId);
    }
}
