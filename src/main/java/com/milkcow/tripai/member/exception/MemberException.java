package com.milkcow.tripai.member.exception;

import com.milkcow.tripai.global.exception.ErrorResultAccessor;
import com.milkcow.tripai.global.result.ResultProvider;
import com.milkcow.tripai.member.result.MemberResult;
import com.milkcow.tripai.member.result.OAuth2Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
/**
 * OAuth2 exception
 * GlobalExceptionHandler에 의해 처리
 */
public class MemberException extends RuntimeException implements ErrorResultAccessor {
    private final MemberResult memberResult;

    @Override
    public ResultProvider getErrorResult() {
        return this.memberResult;
    }
}