package com.milkcow.tripai.global.dto;

import com.milkcow.tripai.global.exception.ApiResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ResponseDto {

    private final Integer code;
    private final Boolean success;
    private final String message;

    public static ResponseDto of(Boolean success, ApiResult result) {
        return new ResponseDto(result.getCode(), success, result.getMessage());
    }

    public static ResponseDto of(Boolean success, ApiResult result, String message) {
        return new ResponseDto(result.getCode(), success, result.getMessage(message));
    }
}
