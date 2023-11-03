package com.milkcow.tripai.article.controller;

import com.milkcow.tripai.article.dto.*;
import com.milkcow.tripai.article.result.CommentResult;
import com.milkcow.tripai.article.service.CommentService;
import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.security.CustomUserDetails;
import com.milkcow.tripai.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CustomUserDetailsService userDetailsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<CommentCreateResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody @Valid CommentCreateRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        CommentCreateResponse response = commentService.create(request, member);

        return DataResponse.create(response, CommentResult.COMMENT_CREATED);
    }

    @PutMapping("/{commentId}")
    public DataResponse<CommentModifyResponse> modify(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable Long commentId,
                                                      @RequestBody @Valid CommentModifyRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        CommentModifyResponse response = commentService.modify(commentId, request, member);

        return DataResponse.create(response);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DataResponse<Void> remove(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable Long commentId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        commentService.remove(commentId, member);

        return DataResponse.create(null, CommentResult.COMMENT_DELETED);
    }
}
