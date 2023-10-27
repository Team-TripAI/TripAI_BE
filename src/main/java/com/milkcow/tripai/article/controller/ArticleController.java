package com.milkcow.tripai.article.controller;

import com.milkcow.tripai.article.dto.ArticleCreateRequest;
import com.milkcow.tripai.article.dto.ArticleCreateResponse;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.article.service.ArticleService;
import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<ArticleCreateResponse> create(@RequestBody @Valid ArticleCreateRequest request) {
        Member member = Member.builder().build();

        ArticleCreateResponse response = articleService.createArticle(request, member);

        return DataResponse.create(response, ArticleResult.ARTICLE_CREATED);
    }
}
