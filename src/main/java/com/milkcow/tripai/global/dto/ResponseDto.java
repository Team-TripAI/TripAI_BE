package com.milkcow.tripai.global.dto;

import com.milkcow.tripai.global.result.ResultProvider;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ResponseDto {

    @ApiParam(value = "결과 코드")
    @Schema(description = "결과 코드", example = "200")
    private final Integer code;

    @ApiParam(value = "성공 여부")
    @Schema(description = "성공 여부", example = "true")
    private final Boolean success;

    @ApiParam(value = "응답 메시지")
    @Schema(description = "응답 메시지", example = "성공")
    private final String message;

    public static ResponseDto of(Boolean success, ResultProvider result) {
        return new ResponseDto(result.getCode(), success, result.getMessage());
    }

    public static ResponseDto of(Boolean success, ResultProvider result, String message) {
        String nonBlankMessage = Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(result.getMessage());
        return new ResponseDto(result.getCode(), success, nonBlankMessage);
    }
}
