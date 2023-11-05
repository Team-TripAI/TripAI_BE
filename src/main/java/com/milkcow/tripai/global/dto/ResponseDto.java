package com.milkcow.tripai.global.dto;

import com.milkcow.tripai.global.result.ResultProvider;
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

    private final Integer code;
    private final Boolean success;
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
