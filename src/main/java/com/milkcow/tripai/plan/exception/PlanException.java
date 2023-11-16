package com.milkcow.tripai.plan.exception;

import com.milkcow.tripai.global.exception.ErrorResultAccessor;
import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlanException extends RuntimeException implements ErrorResultAccessor {
    private final ResultProvider result;

    @Override
    public ResultProvider getErrorResult() {
        return this.result;
    }
}
