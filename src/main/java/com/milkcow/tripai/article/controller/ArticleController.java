package com.milkcow.tripai.article.controller;

import com.milkcow.tripai.article.dto.*;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.article.service.ArticleService;
import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        // TODO - @AuthenticationPrincipal 로 대체
        Member member = Member.builder().build();

        ArticleCreateResponse response = articleService.createArticle(request, member);

        return DataResponse.create(response, ArticleResult.ARTICLE_CREATED);
    }

    @GetMapping
    public DataResponse<ArticlePageResponse> getPage(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());

        ArticlePageResponse response = articleService.getArticlePage(pageRequest);

        return DataResponse.create(response);
    }

    @GetMapping("/{articleId}")
    public DataResponse<ArticleDetailResponse> getDetail(@PathVariable Long articleId) {
        ArticleDetailResponse response = articleService.getArticle(articleId);

        return DataResponse.create(response);
    }

    @PutMapping("/{articleId}")
    public DataResponse<ArticleModifyResponse> modify(@PathVariable Long articleId,
                                                      @RequestBody @Valid ArticleModifyRequest request) {
        // TODO - @AuthenticationPrincipal 로 대체
        Member member = Member.builder().build();

        ArticleModifyResponse response = articleService.modifyArticle(articleId, request, member);

        return DataResponse.create(response);
    }

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DataResponse<Void> remove(@PathVariable Long articleId) {
        // TODO - @AuthenticationPrincipal 로 대체
        Member member = Member.builder().build();

        articleService.removeArticle(articleId, member);

        return DataResponse.empty();
    }
}
