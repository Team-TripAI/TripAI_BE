package com.milkcow.tripai.plan.result;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlanResult implements ResultProvider {
    OK_FLIGHT_PLAN(200, HttpStatus.OK, "항공권 조회 성공"),

    API_KEY_LIMIT_EXCESS(429, HttpStatus.UNAUTHORIZED, "항공권 API 만료"),

    FLIGHT_API_REQUEST_FAILED(510, HttpStatus.INTERNAL_SERVER_ERROR, "항공권 API 요청 실패"),
    FLIGHT_API_RESPONSE_EMPTY(520, HttpStatus.INTERNAL_SERVER_ERROR, "빈 항공권 API응답"),
    FLIGHT_API_RESPONSE_INVALID(530, HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 항공권 API응답"),
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
