package com.milkcow.tripai.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentModifyResponse {
    private final Long commentId;

    public static CommentModifyResponse from(Long commentId) {
        return new CommentModifyResponse(commentId);
    }
}
