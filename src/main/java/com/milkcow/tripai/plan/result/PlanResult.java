package com.milkcow.tripai.plan.result;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 예산기반 일정 추천 결과
 */
@Getter
@RequiredArgsConstructor
public enum PlanResult implements ResultProvider {
    OK_PLAN_REGISTER(201, HttpStatus.CREATED, "일정 등록 성공"),
    OK_PLAN_FIND(200, HttpStatus.OK, "개별 일정 조회 성공"),

    PLAN_FORBIDDEN(403, HttpStatus.FORBIDDEN, "권한 없음"),
    PLAN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 계획"),
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
