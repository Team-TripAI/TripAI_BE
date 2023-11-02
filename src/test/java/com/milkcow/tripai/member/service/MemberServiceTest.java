package com.milkcow.tripai.member.service;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.domain.Password;
import com.milkcow.tripai.member.domain.TripAIPasswordEncoder;
import com.milkcow.tripai.member.dto.MemberSignupRequestDto;
import com.milkcow.tripai.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    void emailSignUp() {


        MemberSignupRequestDto requestDto  = MemberSignupRequestDto.builder()
                .email("wjdghks0706")
                .pw("1234")
                .nickname("park")
                .build();


        Member member = memberService.emailSignUp(requestDto);
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        System.out.println("member.getPassword() = " + member.getPassword());
        Assertions.assertThat(member.getPassword()).isEqualTo(findMember.get().getPassword());

    }
}