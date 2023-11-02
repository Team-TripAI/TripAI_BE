package com.milkcow.tripai.global.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtResult implements ResultProvider {

    FAIL_AUTHENTICATION(418, HttpStatus.FORBIDDEN, "인증 실패!"),

    FAIL_GET_AUTHENTICATION(438, HttpStatus.FORBIDDEN, "토큰 발급 실패!"),

    FAIL_AUTH_PROVIDER(448, HttpStatus.FORBIDDEN, "토큰과 비밀번호 불일치!"),

    TOKEN_NOT_EXIST(400, HttpStatus.UNAUTHORIZED, " 토큰이 존재하지 않습니다."),

    INVALID_ACCESS_TOKEN(499, HttpStatus.UNAUTHORIZED, "유효하지 않은 ACCESS 토큰입니다."),

    EXPIRED_TOKEN(489, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),





    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}