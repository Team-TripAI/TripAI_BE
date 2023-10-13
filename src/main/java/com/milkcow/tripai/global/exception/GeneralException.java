package com.milkcow.tripai.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeneralException extends RuntimeException implements ErrorResultProvider {

    private final ApiResult errorResult;
}
