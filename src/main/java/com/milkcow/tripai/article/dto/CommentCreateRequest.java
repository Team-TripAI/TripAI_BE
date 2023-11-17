package com.milkcow.tripai.article.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema
public class CommentCreateRequest {

    @NotNull
    @ApiParam(value = "게시글 ID")
    @ApiModelProperty(example = "1")
    private final Long articleId;

    @ApiParam(value = "댓글 ID\n" +
            "null 인 경우 댓글 작성 요청, notnull 인 경우 대댓글 작성 요청")
    @ApiModelProperty(example = "1")
    private final Long commentId;

    @NotNull
    @Size(max = 200)
    @ApiParam(value = "댓글 내용\n" +
            "최대 200자")
    @ApiModelProperty(example = "이야 해초 보소")
    private final String content;
}
