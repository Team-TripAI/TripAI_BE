package com.milkcow.tripai.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ApiResult {

    OK(200, HttpStatus.OK, "성공"),
    CREATED(201, HttpStatus.CREATED, "생성 성공"),

    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청"),

    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류"),
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;

    /**
     * 주어진, 공백이 아닌 메시지를 반환하는 메서드이다.
     * 혹은 메시지가 공백인 경우 기본 메시지를 반환한다.
     *
     * @param message 처리할 메시지
     * @return 기본 메시지 혹은 공백이 아닌, 주어진 메시지
     */
    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    /**
     * 주어진 HTTP 상태코드에 대응하는 {@code ApiResult} enum 값을 반환하는 메서드이다.
     *
     * @param httpStatus {@code ApiResult} enum 값을 매핑할 HTTP 상태코드
     * @return 제공된 HTTP 상태코드와 연관된 {@code ApiResult} enum 값, 혹은 매칭되는 것이 없다면 기본값;
     * 4XX 클라이언트 에러 - {@code ApiResult.BAD_REQUEST},
     * 5XX 서버 에러 - {@code ApiResult.INTERNAL_SERVER_ERROR},
     * 그 외 - {@code ApiResult.OK}
     * @throws GeneralException HTTP 상태코드가 null 값이면 경우 발생
     */
    public static ApiResult valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        }

        return Arrays.stream(values())
                .filter(code -> code.getStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ApiResult.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ApiResult.INTERNAL_SERVER_ERROR;
                    } else {
                        return ApiResult.OK;
                    }
                });
    }
}
