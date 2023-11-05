package com.milkcow.tripai.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class CommentDetailResponse {

    private final Long commentId;

    private final String content;

    private final Boolean isParent;

    private final String nickname;

    private final LocalDateTime createDate;

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
