package com.milkcow.tripai.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class CommentCreateRequest {

    @NotNull
    private final Long articleId;

    // null 인 경우 댓글 작성 요청, notnull 인 경우 대댓글 작성 요청
    private final Long commentId;

    @NotNull
    @Size(max = 200)
    private final String content;
}
