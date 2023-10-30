package com.milkcow.tripai.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentCreateResponse {
    private final Long commentId;

    public static CommentCreateResponse from(Long commentId) {
        return new CommentCreateResponse(commentId);
    }
}
