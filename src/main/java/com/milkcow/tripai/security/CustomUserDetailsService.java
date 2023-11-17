package com.milkcow.tripai.security;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.exception.MemberException;
import com.milkcow.tripai.member.repository.MemberRepository;
import com.milkcow.tripai.member.result.MemberResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    /**
     * 로그인 사용자의 정보가 필요할 때 매번 서버에 요청을 보내 DB에 접근해서 데이터를 가져오는 것은 비효율적이다. 따라서 한번 인증된 사용자 정보를 세션에 담아놓고 세션이 유지되는 동안 사용자 객체를
     * DB로 접근하는 방법 없이 바로 사용할 수 있도록 하였다. 컨트롤러에서 @AuthenticationPrincipal 선언하여 엔티티의 어댑터 객체 받아오는 방식
     *
     * @param email
     * @return MemberAdapter
     **/
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new MemberException(MemberResult.NOT_FOUND_MEMBER));

        return new MemberAdapter(member);
    }
}