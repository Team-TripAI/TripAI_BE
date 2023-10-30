package com.milkcow.tripai.article.controller;

import com.milkcow.tripai.article.dto.CommentCreateRequest;
import com.milkcow.tripai.article.dto.CommentCreateResponse;
import com.milkcow.tripai.article.result.CommentResult;
import com.milkcow.tripai.article.service.CommentService;
import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<CommentCreateResponse> create(@RequestBody @Valid CommentCreateRequest request) {
        // TODO - @AuthenticationPrincipal 로 대체
        Member member = Member.builder().build();

        CommentCreateResponse response = commentService.createComment(request, member);

        return DataResponse.create(response, CommentResult.COMMENT_CREATED);
    }
}
