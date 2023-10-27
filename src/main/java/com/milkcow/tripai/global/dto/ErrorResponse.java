package com.milkcow.tripai.global.dto;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * API 실패 시 {@code @RestControllerAdvice}에서 공통적으로 반환하는 클래스이다.
 *
 */
@NoArgsConstructor(force = true)
public class ErrorResponse extends ResponseDto {

    public static ErrorResponse create(ResultProvider errorResult) {
        return new ErrorResponse(errorResult);
    }

    public static ErrorResponse create(ResultProvider errorResult, String message) {
        String nonBlankMessage = Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(errorResult.getMessage());
        return new ErrorResponse(errorResult, nonBlankMessage);
    }

    private ErrorResponse(ResultProvider errorResult) {
        super(errorResult.getCode(), false, errorResult.getMessage());
    }

    private ErrorResponse(ResultProvider errorResult, String message) {
        super(errorResult.getCode(), false, message);
    }
}
