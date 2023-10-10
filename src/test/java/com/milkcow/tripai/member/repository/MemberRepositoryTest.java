package com.milkcow.tripai.member.repository;

import com.milkcow.tripai.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void id_회원조회() throws Exception{
        //given
        Member m1 = Member.builder()
                .email("juns1s@naver.com")
                .password("1234")
                .nickname("junseo")
                .build();
        Member m2 = Member.builder()
                .email("abcd@naver.com")
                .password("4321")
                .nickname("abcd")
                .build();
        //when
        memberRepository.save(m1);
        memberRepository.save(m2);

        //then
        Member member1 = memberRepository.findById(3L).get();
        Member member2 = memberRepository.findById(4L).get();
        Assertions.assertThat(member1.getEmail()).isEqualTo("juns1s@naver.com");
        Assertions.assertThat(member2.getEmail()).isEqualTo("abcd@naver.com");
    }

    @Test
    public void 이메일_회원조회() throws Exception{
        //given
        Member m1 = Member.builder()
                .email("juns1s@naver.com")
                .password("1234")
                .nickname("junseo")
                .build();
        Member m2 = Member.builder()
                .email("abcd@naver.com")
                .password("4321")
                .nickname("abcd")
                .build();
        //when
        memberRepository.save(m1);
        memberRepository.save(m2);

        //then
        Member member1 = memberRepository.findByEmail("juns1s@naver.com").get();
        Member member2 = memberRepository.findByEmail("abcd@naver.com").get();
        Assertions.assertThat(member1.getEmail()).isEqualTo("juns1s@naver.com");
        Assertions.assertThat(member2.getEmail()).isEqualTo("abcd@naver.com");
    }
}