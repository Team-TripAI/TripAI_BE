package com.milkcow.tripai.article.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentSearch {

    private final Long commentId;

    private final String content;

    private final Long parentId;

    private final String nickname;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    @QueryProjection
    public CommentSearch(Long commentId, Long parentId, String content, String nickname, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.content = content;
        this.nickname = nickname;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }
}
