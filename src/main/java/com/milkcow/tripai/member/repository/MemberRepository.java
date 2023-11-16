package com.milkcow.tripai.member.repository;

import com.milkcow.tripai.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Member findByRefreshToken(String refreshToken);

    boolean existsByEmailAndNickname(String email, String nickname);
}
