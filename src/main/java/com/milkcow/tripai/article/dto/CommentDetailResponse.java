package com.milkcow.tripai.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class CommentDetailResponse {

    @ApiParam(value = "댓글 ID")
    @Schema(description = "조회된 댓글 ID", example = "1")
    private final Long commentId;

    @ApiParam(value = "댓글 내용")
    @Schema(description = "조회된 댓글 내용", example = "이야 해초 보소")
    private final String content;

    @ApiParam(value = "댓글/대댓글 여부")
    @Schema(description = "댓글: true, 대댓글: false", example = "true")
    private final Boolean isParent;

    @ApiParam(value = "작성자 이름")
    @Schema(description = "작성자 이름", example = "정준서")
    private final String nickname;

    @ApiParam(value = "작성일자")
    @Schema(description = "작성일자", example = "2023-10-31T01:01:22.561065")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createDate;

    @ApiParam(value = "최종수정일자")
    @Schema(description = "최종수정일자", example = "2023-10-31T01:01:22.561065")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifyDate;

    public static CommentDetailResponse from(CommentSearch searchedComment) {
        return new CommentDetailResponse(
                searchedComment.getCommentId(),
                searchedComment.getContent(),
                searchedComment.getCommentId().equals(searchedComment.getParentId()),
                searchedComment.getNickname(),
                searchedComment.getCreateDate(),
                searchedComment.getModifyDate()
        );
    }
}
