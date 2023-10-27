package com.milkcow.tripai.article.exception;

import com.milkcow.tripai.global.exception.ErrorResultAccessor;
import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticleException extends RuntimeException implements ErrorResultAccessor {

    private final ResultProvider errorResult;
}
