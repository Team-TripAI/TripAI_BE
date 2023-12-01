package com.milkcow.tripai.plan.repository;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.plan.domain.Plan;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 예산기반 일정 추천 리포지토리
 */
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findPlanById(Long id);

    Page<Plan> findAllByMember(PageRequest pageRequest, Member member);
}
