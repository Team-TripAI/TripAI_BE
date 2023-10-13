package com.milkcow.tripai.global.dto;

import com.milkcow.tripai.global.exception.ApiResult;

/**
 * API 실패 시 {@code @RestControllerAdvice}에서 공통적으로 반환하는 클래스이다.
 *
 */
public class ErrorResponse extends ResponseDto {

    public static ErrorResponse create(ApiResult errorResult) {
        return new ErrorResponse(errorResult);
    }

    public static ErrorResponse create(ApiResult errorResult, String message) {
        return new ErrorResponse(errorResult, message);
    }

    private ErrorResponse(ApiResult errorResult) {
        super(errorResult.getCode(), false, errorResult.getMessage());
    }

    private ErrorResponse(ApiResult errorResult, String message) {
        super(errorResult.getCode(), false, errorResult.getMessage(message));
    }
}
