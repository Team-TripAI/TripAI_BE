package com.milkcow.tripai.article.controller;

import com.milkcow.tripai.article.dto.*;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.article.service.ArticleService;
import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.security.CustomUserDetails;
import com.milkcow.tripai.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<ArticleCreateResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody @Valid ArticleCreateRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        ArticleCreateResponse response = articleService.create(request, member);

        return DataResponse.create(response, ArticleResult.ARTICLE_CREATED);
    }

    @GetMapping("/articles")
    public DataResponse<ArticlePageResponse> getPage(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());

        ArticlePageResponse response = articleService.getPage(pageRequest);

        return DataResponse.create(response);
    }

    @GetMapping("/users/articles")
    public DataResponse<ArticlePageResponse> getPage(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam(defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());

        ArticlePageResponse response = articleService.getPage(pageRequest, member);

        return DataResponse.create(response);
    }

    @GetMapping("/articles/{articleId}")
    public DataResponse<ArticleDetailResponse> getDetail(@PathVariable Long articleId) {
        ArticleDetailResponse response = articleService.getDetail(articleId);

        return DataResponse.create(response);
    }

    @DeleteMapping("/articles/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DataResponse<Void> remove(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable Long articleId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        articleService.remove(articleId, member);

        return DataResponse.empty();
    }
}
