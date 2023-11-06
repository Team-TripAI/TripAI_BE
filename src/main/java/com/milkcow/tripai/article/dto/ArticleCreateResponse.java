package com.milkcow.tripai.article.dto;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticleCreateResponse {

    @ApiParam(value = "작성된 게시글 ID")
    @Schema(description = "작성된 게시글 ID", example = "1")
    private final Long articleId;

    public static ArticleCreateResponse from(Long articleId) {
        return new ArticleCreateResponse(articleId);
    }
}
