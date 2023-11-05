package com.milkcow.tripai.image.exception;

import com.milkcow.tripai.global.exception.ErrorResultAccessor;
import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageException extends RuntimeException implements ErrorResultAccessor {

    private final ResultProvider errorResult;
}
