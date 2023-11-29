package com.milkcow.tripai.plan.controller;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.global.dto.ResponseDto;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.plan.dto.PlanRequestDto;
import com.milkcow.tripai.plan.dto.PlanResponseDto;
import com.milkcow.tripai.plan.result.PlanResult;
import com.milkcow.tripai.plan.service.PlanService;
import com.milkcow.tripai.security.CustomUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 예산기반 일정 컨트롤러
 */
@Api(tags = "PlanControler", description = "예산 기반 일정 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/plan/budget")
@Slf4j
public class PlanController {

    private final PlanService planService;
    private final UserDetailsService userDetailsService;

    /**
     * 예산 기반 일정 등록
     * @param userDetails {@link CustomUserDetails} 사용자 정보
     * @param planDto {@link PlanRequestDto} 예산 기반 일정 정보
     * @return HttpStatus.CREATED
     */
    @ApiOperation(value = "예산 기반 일정 등록", notes = "예산 기반 일정 등록 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "planDto", value = "등록할 예산기반 일정 정보", readOnly = true, paramType = "body", dataTypeClass = PlanRequestDto.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "일정 등록 성공"),
            @ApiResponse(code = 403, message = "로그인 필요"),
            @ApiResponse(code = 500, message = "서버 내 요류"),
    })
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto create(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody PlanRequestDto planDto) {

        CustomUserDetails customUserDetails = (CustomUserDetails) (userDetailsService.loadUserByUsername(
                userDetails.getUsername()));
        Member member = customUserDetails.getMember();
        long id = planService.create(member, planDto);
        return ResponseDto.of(true, PlanResult.OK_PLAN_CREATE);
    }

    /**
     * 예산 기반 일정 개별 조회
     * @param userDetails {@link CustomUserDetails} 사용자 정보
     * @param planId 해당 일정의 id
     * @return {@link PlanResponseDto} 일정 정보
     */
    @ApiOperation(value = "예산 기반 일정 조회", notes = "예산 기반 일정 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "planId", value = "조회할 예산기반 일정 ID", readOnly = true, paramType = "query", dataType = "int", example = "1")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "개별 일정 조회 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 계획"),
            @ApiResponse(code = 500, message = "서버 내 요류"),
    })
    @GetMapping("/{planId}")
    public DataResponse<PlanResponseDto> getPlan(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long planId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) (userDetailsService.loadUserByUsername(
                userDetails.getUsername()));
        Member member = customUserDetails.getMember();
        PlanResponseDto planDto = planService.find(member, planId);

        return DataResponse.create(planDto, PlanResult.OK_PLAN_FIND);
    }

    /**
     * 예산 기반 일정 삭제
     * @param userDetails {@link CustomUserDetails} 사용자 정보
     * @param planId 해당 일정 id
     * @return HttpStatus.OK
     */
    @ApiOperation(value = "예산 기반 일정 삭제", notes = "예산 기반 일정 삭제 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "planId", value = "조회할 예산기반 일정 ID", readOnly = true, paramType = "query", dataType = "int", example = "1")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "일정 삭제 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 계획"),
            @ApiResponse(code = 500, message = "서버 내 요류"),
    })
    @DeleteMapping("/{planId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto delete(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long planId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) (userDetailsService.loadUserByUsername(
                userDetails.getUsername()));
        Member member = customUserDetails.getMember();
        planService.delete(member, planId);

        return ResponseDto.of(true, PlanResult.OK_PLAN_DELETE);
    }
}
