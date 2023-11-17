package com.milkcow.tripai.plan.controller;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.global.dto.ResponseDto;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.plan.dto.PlanRequestDto;
import com.milkcow.tripai.plan.dto.PlanResponseDto;
import com.milkcow.tripai.plan.result.PlanResult;
import com.milkcow.tripai.plan.service.PlanService;
import com.milkcow.tripai.security.CustomUserDetails;
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
     * @param planID 해당 일정의 id
     * @return {@link PlanResponseDto} 일정 정보
     */
    @GetMapping("/{planID}")
    public DataResponse<PlanResponseDto> getPlan(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long planID) {
        CustomUserDetails customUserDetails = (CustomUserDetails) (userDetailsService.loadUserByUsername(
                userDetails.getUsername()));
        Member member = customUserDetails.getMember();
        PlanResponseDto planDto = planService.find(member, planID);

        return DataResponse.create(planDto, PlanResult.OK_PLAN_FIND);
    }

    /**
     * 예산 기반 일정 삭제
     * @param userDetails {@link CustomUserDetails} 사용자 정보
     * @param planId 해당 일정 id
     * @return HttpStatus.OK
     */
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
