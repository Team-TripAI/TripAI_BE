package com.milkcow.tripai.global.exception;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeneralException extends RuntimeException implements ErrorResultAccessor {

    private final ResultProvider errorResult;
}
