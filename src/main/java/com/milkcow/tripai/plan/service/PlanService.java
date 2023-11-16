package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.plan.domain.Plan;
import com.milkcow.tripai.plan.dto.PlanDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.repository.PlanRepository;
import com.milkcow.tripai.plan.result.PlanResult;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예산 기반 일정 추천 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;

    /**
     * 예산 기반 일정 서비스 등록
     * @param member {@link Member} 일정 등록 사용자
     * @param planDto {@link PlanDto} 일정 등록 DTO
     * @return 해당 일정의 id
     */
    @Transactional
    public long create(Member member, PlanDto planDto) {
        Plan plan = Plan.of(planDto, member);
        Plan saved_plan = planRepository.save(plan);
        return saved_plan.getId();
    }

    /**
     * 예산 기반 일정 개별 조회
     * @param member 일정 등록 사용자
     * @param planId 일정 id
     * @return {@link PlanDto}
     * @throws PlanException 등록한 사용자가 아닌 다른 사용자가 조회하려는 경우
     */
    public PlanDto find(Member member, Long planId) {
        Plan plan = planRepository.findPlanById(planId)
                .orElseThrow(() -> new PlanException(PlanResult.PLAN_NOT_FOUND));

        if (!Objects.equals(member.getId(), plan.getMember().getId())) {
            throw new PlanException(PlanResult.PLAN_FORBIDDEN);
        }
        return PlanDto.from(plan);
    }


    /**
     * 예산 기반 일정 삭제
     * @param member 일정 등록 사용자
     * @param planId 일정 id
     * @throws PlanException 등록한 사용자가 아닌 다른 사용자가 삭제하려는 경우
     */
    @Transactional
    public void delete(Member member, Long planId) {
        Plan plan = planRepository.findPlanById(planId)
                .orElseThrow(() -> new PlanException(PlanResult.PLAN_NOT_FOUND));

        if (!Objects.equals(member.getId(), plan.getMember().getId())) {
            throw new PlanException(PlanResult.PLAN_FORBIDDEN);
        }
        planRepository.deleteById(planId);
    }

}
