package com.milkcow.tripai.article.controller;

import com.milkcow.tripai.article.dto.*;
import com.milkcow.tripai.article.result.CommentResult;
import com.milkcow.tripai.article.service.CommentService;
import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.security.CustomUserDetails;
import com.milkcow.tripai.security.CustomUserDetailsService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Api(tags = "Comment", description = "댓글/대댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    private final CustomUserDetailsService userDetailsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "댓글/대댓글 작성", notes = "댓글 혹은 대댓글을 DB에 작성한다.\n" +
            "- JWT 필요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request", value="저장할 댓글 정보", required = true, paramType = "body", dataTypeClass = CommentCreateRequest.class)
    })
    @ApiResponses({
            @ApiResponse(code = 201, message = "댓글 등록 성공"),
            @ApiResponse(code = 400, message = "부모 댓글이 아님 - 지정된 부모 댓글이 대댓글인 경우 발생"),
            @ApiResponse(code = 404, message = "게시글 찾을 수 없음"),
            @ApiResponse(code = 404, message = "댓글 찾을 수 없음 - 지정된 부모 댓글이 존재하지 않는 경우 발생"),
            @ApiResponse(code = 500, message = "서버 내 오류")
    })
    public DataResponse<CommentCreateResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody @Valid CommentCreateRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        CommentCreateResponse response = commentService.create(request, member);

        return DataResponse.create(response, CommentResult.COMMENT_CREATED);
    }

    @PutMapping("/{commentId}")
    @ApiOperation(value = "댓글/대댓글 수정", notes = "댓글 혹은 대댓글을 수정한다.\n" +
            "- JWT 필요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value="수정할 댓글/대댓글 ID", example = "1", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "request", value="수정할 댓글 정보", required = true, paramType = "body", dataTypeClass = CommentModifyRequest.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "댓글 수정 성공"),
            @ApiResponse(code = 400, message = "댓글 작성자가 아님"),
            @ApiResponse(code = 404, message = "댓글 찾을 수 없음"),
            @ApiResponse(code = 404, message = "이미 삭제된 댓글"),
            @ApiResponse(code = 500, message = "서버 내 오류")
    })
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
    @ApiOperation(value = "댓글/대댓글 삭제", notes = "댓글 혹은 대댓글의 내용과 작성자 정보를 지운다.\n" +
            "- DB에 댓글 자체는 남아있기 때문에, 댓글 조회 시에 함께 조회된다.\n" +
            "- JWT 필요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value="삭제할 댓글/대댓글 ID", example = "1", required = true, paramType = "path", dataTypeClass = Long.class),
    })
    @ApiResponses({
            @ApiResponse(code = 204, message = "댓글 삭제 성공"),
            @ApiResponse(code = 400, message = "댓글 작성자가 아님"),
            @ApiResponse(code = 404, message = "댓글 찾을 수 없음"),
            @ApiResponse(code = 404, message = "이미 삭제된 댓글"),
            @ApiResponse(code = 500, message = "서버 내 오류")
    })
    public DataResponse<Void> remove(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable Long commentId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        commentService.remove(commentId, member);

        return DataResponse.create(null, CommentResult.COMMENT_DELETED);
    }
}
