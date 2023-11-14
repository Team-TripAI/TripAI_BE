package com.milkcow.tripai.security.filter;


import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.milkcow.tripai.jwt.AccessTokenProvider;
import com.milkcow.tripai.jwt.JwtService;
import com.milkcow.tripai.member.exception.MemberException;
import com.milkcow.tripai.member.repository.MemberRepository;
import com.milkcow.tripai.member.result.MemberResult;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class CustomAuthorizationFilter extends BasicAuthenticationFilter {


    private final MemberRepository memberRepository;

    private final JwtService jwtService;

    public CustomAuthorizationFilter(AuthenticationManager authenticationManager,
                                     MemberRepository memberRepository,
                                     JwtService jwtService) {

        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }


    // 인증이나 권한이 필요한 주소요청이 있을 때
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String servletPath = request.getServletPath();
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            //토큰이 없을시는 그대로 진행
            chain.doFilter(request, response);
        } else if (!authorizationHeader.startsWith("Bearer ")) {
            // 정상적이지 않다면 400 오류
            response.setStatus(SC_BAD_REQUEST);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
        } else {
            try {
                // 2. JWT 토큰을 검증해서 정상적인 사용자인지 확인
                String jwtToken = jwtService.extractAccessToken(request);
                String email = jwtService.extractEmail(jwtToken);

                if (email != null) {
                    memberRepository.findByEmail(email).orElseThrow(() ->
                            new MemberException(MemberResult.NOT_FOUND_MEMBER));

                    Authentication authentication = AccessTokenProvider.getAuthentication(jwtToken);

                    // 시큐리티의 세션 영역에 접근하여 Authentication 객체 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (TokenExpiredException e) {
                response.setStatus(SC_UNAUTHORIZED);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");

            } catch (Exception e) {
                response.setStatus(SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");

            } finally {
                chain.doFilter(request, response);
            }
        }

    }
}
