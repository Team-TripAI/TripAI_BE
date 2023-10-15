package com.milkcow.tripai.member.service;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OAuth2ServiceTest {
    @Autowired
    private OAuth2Service oAuth2Service;
    @Autowired
    private MemberRepository memberRepository;
    private String token = "accessToken";
    @Test
    public void 구글_회원가입() throws Exception{
        //given

        //when
        oAuth2Service.oAuth2Login(token, null);
        //then
        Member member = memberRepository.findByEmail("junjuns1s1s@gmail.com").get();
        System.out.println("member.getNickname() = " + member.getNickname());
        Assertions.assertThat(member.getEmail()).isEqualTo("junjuns1s1s@gmail.com");
    }
    @Test
    public void 구글_로그인() throws Exception{
        //given
        oAuth2Service.oAuth2Login(token, null);
        oAuth2Service.oAuth2Login(token, null);
        //when
        //then
    }
}