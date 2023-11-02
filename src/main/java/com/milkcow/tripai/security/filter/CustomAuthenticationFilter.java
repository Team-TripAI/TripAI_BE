package com.milkcow.tripai.security.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.global.exception.JwtException;
import com.milkcow.tripai.global.result.JwtResult;
import com.milkcow.tripai.jwt.JwtService;
import com.milkcow.tripai.member.dto.MemberSignupRequestDto;
import com.milkcow.tripai.member.exception.MemberException;
import com.milkcow.tripai.member.result.MemberResult;
import com.milkcow.tripai.security.MemberAdapter;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    /**
     * 지정된 URL로 form 전송을 하였을 경우 파라미터 정보를 가져온다.
     *
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a redirect as part of a
     *                 multi-stage authentication process (such as OpenID).
     * @return Authentication {}
     * @throws AuthenticationException {}
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        logger.info("인증");
        logger.info("1. attemptAuthentication");
        try {
            authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            throw new JwtException(JwtResult.FAIL_AUTHENTICATION);
        }

    }


    /**
     * Request로 받은 ID와 패스워드 기반으로 토큰을 발급한다.
     *
     * @param request HttpServletRequest
     * @return UsernamePasswordAuthenticationToken
     * @throws Exception e
     */
    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            MemberSignupRequestDto requestDto = objectMapper.readValue(request.getInputStream(),
                    MemberSignupRequestDto.class);

            logger.info("getAuthRequest :: userId:" + requestDto.getEmail() + " userPw:"
                    + requestDto.getPw());

            // ID와 패스워드를 기반으로 토큰 발급
            return new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPw());
        } catch (UsernameNotFoundException ae) {
            throw new MemberException(MemberResult.NOT_FOUND_MEMBER);
        } catch (Exception e) {
            throw new JwtException(JwtResult.FAIL_GET_AUTHENTICATION);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        logger.info("successfulAuthentication");
        MemberAdapter memberAdapter = (MemberAdapter) authentication.getPrincipal();

        // 1. JWT토큰 생성
        String accessToken = jwtService.createAccessToken(authentication);
        String refreshToken = jwtService.createRefreshToken(authentication);

        jwtService.updateRefreshToken(memberAdapter.getMember().getEmail(), refreshToken);

        // 2. header에 JWT토큰을 넣어서 응답
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);


    }

}