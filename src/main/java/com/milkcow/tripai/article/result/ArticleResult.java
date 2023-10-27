package com.milkcow.tripai.article.result;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ArticleResult implements ResultProvider {

    ARTICLE_CREATED(201, HttpStatus.CREATED, "게시글 등록 성공"),

    NULL_USER_ENTITY(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류"),
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
