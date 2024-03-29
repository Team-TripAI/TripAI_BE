package com.milkcow.tripai.article.result;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentResult implements ResultProvider {

    COMMENT_CREATED(201, HttpStatus.CREATED, "댓글 등록 성공"),
    COMMENT_DELETED(204, HttpStatus.NO_CONTENT, "댓글 삭제 성공"),

    NOT_COMMENT_OWNER(400, HttpStatus.BAD_REQUEST, "댓글 작성자가 아님"),
    NOT_PARENT_COMMENT(400, HttpStatus.BAD_REQUEST, "부모 댓글이 아님"),
    COMMENT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "댓글 찾을 수 없음"),
    ALREADY_REMOVED(404, HttpStatus.NOT_FOUND, "이미 삭제된 댓글"),

    NULL_USER_ENTITY(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류"),
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
