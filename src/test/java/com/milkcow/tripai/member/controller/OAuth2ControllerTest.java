package com.milkcow.tripai.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.dto.GoogleLoginDto;
import com.milkcow.tripai.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OAuth2ControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void mockMvcSetUP(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void 구글로그인_컨트롤러() throws Exception{
        //given
        final String url = "/login/google";
        String token = "ya29.a0AfB_byAvWoXXmdpVxNj9vNLKEVkkOZHImyRzZ5AXLFeM3D8-x8ApYywcfIhZflh0bBcsfYrRw2qAWHIgm9VxmqSWB3krkdRuHUvDQl6xd_3k5AL-hiMYxp-gf34ohH2bxuhL6MnnhMmiyWL85sweQVWIUTqvyeQIe_4aCgYKAQ4SARASFQGOcNnCQqJyUb0kdhhiWvpSv6qpcQ0170";
        GoogleLoginDto loginDto = new GoogleLoginDto(token);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(loginDto);

        //when
        final ResultActions result = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
        Member member = memberRepository.findByEmail("junjuns1s1s@gmail.com").get();
        Assertions.assertThat(member.getEmail()).isEqualTo("junjuns1s1s@gmail.com");
    }
}