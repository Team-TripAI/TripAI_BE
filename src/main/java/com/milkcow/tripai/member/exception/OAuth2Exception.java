package com.milkcow.tripai.member.exception;

import com.milkcow.tripai.global.exception.ErrorResultAccessor;
import com.milkcow.tripai.member.result.OAuth2Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
/**
 * OAuth2 exception
 * GlobalExceptionHandler에 의해 처리
 */
public class OAuth2Exception extends RuntimeException implements ErrorResultAccessor {
    private final OAuth2Result errorResult;
}
