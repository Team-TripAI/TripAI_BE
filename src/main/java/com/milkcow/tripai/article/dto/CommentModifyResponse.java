package com.milkcow.tripai.article.dto;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentModifyResponse {

    @ApiParam(value = "수정된 댓글 ID")
    @Schema(description = "수정된 댓글 ID", example = "1")
    private final Long commentId;

    public static CommentModifyResponse from(Long commentId) {
        return new CommentModifyResponse(commentId);
    }
}
