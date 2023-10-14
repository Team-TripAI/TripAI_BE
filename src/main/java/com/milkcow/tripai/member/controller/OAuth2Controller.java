package com.milkcow.tripai.member.controller;

import com.milkcow.tripai.global.dto.ResponseDto;
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
    public ResponseDto googleLogin(@RequestBody GoogleLoginDto loginDto, HttpServletResponse response) {
        return oAuth2Service.oAuth2Login(loginDto.getToken(), response);
    }

    /**
     * !!! 프론트엔드 완성 전까지만 있을 예정!!!
     * 리다이렉트를 통해 authorize code를 얻는다.
     * 이 코드를 통해 accessToken발급 가능.
     * @param code (String, Authorize Code)
     * @return (String)
     */
    @GetMapping("/test")
    public String testLogin(@RequestParam String code){
        System.out.println("code = " + code);
        return code;
    }
}
