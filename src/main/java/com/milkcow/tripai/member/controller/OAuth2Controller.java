package com.milkcow.tripai.member.controller;

import com.milkcow.tripai.member.dto.GoogleLoginDto;
import com.milkcow.tripai.member.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;

    /**
     * googleLogin<p>
     * google OAuth2서버의 access token을 받아 회원정보를 통해 로그인 또는 회원가입 진행
     * @param loginDto (GoogleLoginDto, google OAuth2서버의 accessToken)
     * @param response (HttpServletResponse)
     */
    @PostMapping("/login/google")
    @ResponseStatus(HttpStatus.OK)
    public void googleLogin(@RequestBody GoogleLoginDto loginDto, HttpServletResponse response) {
        oAuth2Service.oAuth2Login(loginDto.getToken(), response);
    }
    @GetMapping("/test")
    public String testLogin(@RequestParam String code){
        System.out.println("code = " + code);
        return code;
    }
}
