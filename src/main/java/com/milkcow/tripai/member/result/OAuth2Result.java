package com.milkcow.tripai.member.result;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OAuth2Result implements ResultProvider {
    OK_GOOGLE_LOGIN(200, HttpStatus.OK, "구글 로그인 성공"),

    INVALID_ACCESS_TOKEN(413, HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token"),

    FAIL_TO_ACCESS_GOOGLE_API(510, HttpStatus.INTERNAL_SERVER_ERROR, "구글 OAuth2 API 요청 실패"),

    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
