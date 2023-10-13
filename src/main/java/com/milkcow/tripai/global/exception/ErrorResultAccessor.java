package com.milkcow.tripai.global.exception;

import com.milkcow.tripai.global.result.ResultProvider;

/**
 * 사용자 정의 예외 클래스가 반드시 ResultProvider 구현체에 접근할 수 있도록 강제하기 위한 인터페이스이다.
 */
public interface ErrorResultAccessor {
    ResultProvider getErrorResult();
}
