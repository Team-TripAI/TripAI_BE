package com.milkcow.tripai.member.controller;

import com.milkcow.tripai.global.dto.ResponseDto;
import com.milkcow.tripai.member.dto.GoogleLoginDto;
import com.milkcow.tripai.member.service.OAuth2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "OAuth2Controller", description = "OAuth2를 통한 로그인 및 회원가입 API")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;

    /**
     * googleLogin<p> google OAuth2서버의 access token을 받아 회원정보를 통해 로그인 또는 회원가입 진행
     *
     * @param loginDto {@link GoogleLoginDto} google OAuth2서버의 accessToken
     * @param response {@link ResponseDto}
     */
    @ApiOperation(value = "구글 로그인 API", notes = "google OAuth2서버의 access token을 받아 회원정보를 통해 로그인 또는 회원가입 진행")
    @ApiImplicitParam(name = "loginDto", value = "구글 OAuth2 access token", readOnly = true, dataType = "string", paramType = "body")
    @PostMapping("/login/google")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto googleLogin(@RequestBody GoogleLoginDto loginDto, HttpServletResponse response) {
        return oAuth2Service.oAuth2Login(loginDto.getToken(), response);
    }

    /**
     * !!! 프론트엔드 완성 전까지만 있을 예정!!! 리다이렉트를 통해 authorize code를 얻는다. 이 코드를 통해 accessToken발급 가능.
     *
     * @param code (String, Authorize Code)
     * @return (String)
     */
    @GetMapping("/test")
    public String testLogin(@RequestParam String code) {
        System.out.println("code = " + code);
        return code;
    }
}
