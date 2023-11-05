package com.milkcow.tripai.global.dto;

import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API 성공 시 컨트롤러에서 공통적으로 반환하는 클래스이다.
 * 기본적으로 HTTP OK로 전달되며, 상태코드를 바꾸어야 하는 경우 {@code @ResponseStatus}를 사용한다.
 *
 * @param <T> API 성공 시 반환하는 데이터의 타입
 */
@Getter
@NoArgsConstructor(force = true)
public class DataResponse<T> extends ResponseDto {

    private final T data;

    public static <T> DataResponse<T> create(T data) {
        return new DataResponse<>(data);
    }

    public static <T> DataResponse<T> create(T data, String message) {
        return new DataResponse<>(data, message);
    }

    public static <T> DataResponse<T> create(T data, ResultProvider result) {
        return new DataResponse<>(data, result);
    }

    public static <T> DataResponse<T> empty() {
        return new DataResponse<>(null);
    }

    private DataResponse(T data) {
        super(ApiResult.OK.getCode(), true, ApiResult.OK.getMessage());
        this.data = data;
    }

    private DataResponse(T data, String message) {
        super(ApiResult.OK.getCode(), true, message);
        this.data = data;
    }

    private DataResponse(T data, ResultProvider result) {
        super(result.getCode(), true, result.getMessage());
        this.data = data;
    }
}
