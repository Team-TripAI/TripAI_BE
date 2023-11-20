package com.milkcow.tripai.image.result;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageResult implements ResultProvider {

    INVALID_COLOR_PATTERN(400, HttpStatus.BAD_REQUEST, "유효하지 않은 색상값"),
    IMAGE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "이미지 찾을 수 없음"),
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}