package com.milkcow.tripai.article.controller;

import com.milkcow.tripai.article.dto.*;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.article.service.ArticleService;
import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.security.CustomUserDetails;
import com.milkcow.tripai.security.CustomUserDetailsService;
import io.swagger.annotations.*;
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
@Api(tags = "Article", description = "게시글 관련 API")
public class ArticleController {

    private final ArticleService articleService;

    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/articles")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "게시글 작성", notes = "게시글 정보를 DB에 작성한다.\n" +
            "- Vision AI을 통해 분석된 이미지 정보와, 주소 정보 필요\n" +
            "- JWT 필요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request", value="저장할 게시글 정보", required = true, paramType = "body", dataTypeClass = ArticleCreateRequest.class)
    })
    @ApiResponses({
            @ApiResponse(code = 201, message = "게시글 등록 성공"),
            @ApiResponse(code = 500, message = "서버 내 오류")
    })
    public DataResponse<ArticleCreateResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody @Valid ArticleCreateRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        ArticleCreateResponse response = articleService.create(request, member);

        return DataResponse.create(response, ArticleResult.ARTICLE_CREATED);
    }

    @GetMapping("/articles")
    @ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 최신순으로 조회한다.\n" +
            "- JWT 필요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value="조회할 페이지 (0부터 시작)", example = "0", paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value="한 페이지에 조회할 개수", example = "10", paramType = "query", dataTypeClass = Integer.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 목록 조회 성공"),
            @ApiResponse(code = 500, message = "서버 내 오류")
    })
    public DataResponse<ArticlePageResponse> getPage(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());

        ArticlePageResponse response = articleService.getPage(pageRequest);

        return DataResponse.create(response);
    }

    @GetMapping("/users/articles")
    @ApiOperation(value = "내가 작성한 게시글 목록 조회", notes = "요청하는 사용자가 작성한 게시글 목록을 최신순으로 조회한다.\n" +
            "- JWT 필요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value="조회할 페이지 (0부터 시작)", example = "0", paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value="한 페이지에 조회할 개수", example = "10", paramType = "query", dataTypeClass = Integer.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 조회 성공"),
            @ApiResponse(code = 500, message = "서버 내 오류")
    })
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
    @ApiOperation(value = "게시글 상세 조회", notes = "게시글에 대한 상세 정보와 오래된 순으로 정렬된 댓글 목록을 조회한다.\n" +
            "- 삭제된 댓글의 경우 content와 nickname이 null이기 때문에 이에 대한 처리가 필요\n" +
            "- JWT 필요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value="조회할 게시글 ID", example = "1", required = true, paramType = "path", dataTypeClass = Long.class),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 조회 성공"),
            @ApiResponse(code = 404, message = "게시글 찾을 수 없음"),
            @ApiResponse(code = 500, message = "서버 내 오류")
    })
    public DataResponse<ArticleDetailResponse> getDetail(@PathVariable Long articleId) {
        ArticleDetailResponse response = articleService.getDetail(articleId);

        return DataResponse.create(response);
    }

    @DeleteMapping("/articles/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "게시글 삭제", notes = "게시글 ID를 사용해서 해당 게시글을 삭제한다.\n" +
            "- Path variable로 전달할 것 (e.g. /articles/1)\n" +
            "- JWT 필요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value="삭제할 게시글 ID", example = "1", required = true, paramType = "path", dataTypeClass = Long.class),
    })
    @ApiResponses({
            @ApiResponse(code = 204, message = "게시글 삭제 성공"),
            @ApiResponse(code = 400, message = "게시글 작성자가 아님"),
            @ApiResponse(code = 404, message = "게시글 찾을 수 없음"),
            @ApiResponse(code = 500, message = "서버 내 오류")
    })
    public DataResponse<Void> remove(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable Long articleId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
                userDetails.getUsername());
        Member member = customUserDetails.getMember();

        articleService.remove(articleId, member);

        return DataResponse.empty();
    }
}
