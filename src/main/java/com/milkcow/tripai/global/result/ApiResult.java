package com.milkcow.tripai.global.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiResult implements ResultProvider {

    OK(200, HttpStatus.OK, "성공"),
    CREATED(201, HttpStatus.CREATED, "생성 성공"),

    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청"),

    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류"),
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
