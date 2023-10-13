package com.milkcow.tripai.global.exception;

/**
 * 사용자 정의 예외 클래스가 반드시 ApiResult 인스턴스와 그에 대한 getter를 보유하도록 강제하기 위한 인터페이스이다.
 */
public interface ErrorResultProvider {
    ApiResult getErrorResult();
}
