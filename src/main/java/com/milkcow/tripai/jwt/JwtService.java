package com.milkcow.tripai.jwt;

import static com.milkcow.tripai.jwt.AccessTokenProvider.AUTHORITIES_KEY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.exception.JwtException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.global.result.JwtResult;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.exception.MemberException;
import com.milkcow.tripai.member.repository.MemberRepository;
import com.milkcow.tripai.member.result.MemberResult;
import com.milkcow.tripai.security.MemberAdapter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
@Transactional(readOnly = true)
public class JwtService {

    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정 JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰}
     */

    private static final String COOKIE_PATH = "/";
    private static final String COOKIE_DOMAIN = "www.tripai.site";

    @Value("${jwt.header}")
    private String ACCESS_HEADER;

    @Value("${jwt.secret}")
    private String SECRET;

    private static final String REFRESH_HEADER = "Authorization_refresh";

    private static final String ROLE_USER = "USER";

    private final MemberRepository memberRepository;

    private final AccessTokenProvider accessTokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(Authentication authentication) {

        MemberAdapter memberAdapter = (MemberAdapter) authentication.getPrincipal();

        long now = (new Date()).getTime();
        Date valid = new Date(now + accessTokenProvider.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, ROLE_USER)
                .claim("user_id", memberAdapter.getMember().getId())
                .claim("user_email", memberAdapter.getUsername())
                .signWith(accessTokenProvider.key, SignatureAlgorithm.HS512)
                .setExpiration(valid)
                .compact();
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + refreshTokenProvider.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .signWith(refreshTokenProvider.key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * AccessToken은 헤더에, RefreshToken은 쿠키에 보내기.
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenCookie(response, refreshToken);
        log.info("Access Token 헤더, Refresh Token 쿠키 설정 완료.");
    }

    /**
     * AccessToken에서 Email 추출 추출 전에 JWT.require() verify로 검증 후 이메일 추출
     *
     * @param accessToken
     * @return {@link Optional} containing the {@link String} email
     */
    public String extractEmail(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return JWT.require(Algorithm.HMAC512(accessTokenProvider.key.getEncoded())).build().verify(accessToken)
                    .getClaim("user_email").asString();
        } catch (Exception e) {
            throw new JwtException(JwtResult.INVALID_ACCESS_TOKEN);
        }
    }

    /**
     * 쿠키에서 RefreshToken 추출 토큰 형식 : Bearer XXX -> "" XXX
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        Cookie refreshCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(REFRESH_HEADER))
                .findFirst()
                .orElse(null);
        if (refreshCookie == null) {
            throw new GeneralException(ApiResult.BAD_REQUEST);
        }
        return Optional.ofNullable(refreshCookie.getValue());
    }

    /**
     * 헤더에서 AccessToken 추출 토큰 형식 : Bearer XXX -> "" XXX
     */
    public String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_HEADER, accessToken);
    }

    /**
     * RefreshToken 쿠키 설정
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_HEADER, refreshToken);
        if (refreshTokenProvider.tokenValidityInMilliseconds > Integer.MAX_VALUE) {
            throw new JwtException(JwtResult.INVALID_PERIOD);
        } else if (refreshTokenProvider.tokenValidityInMilliseconds < Integer.MIN_VALUE) {
            throw new JwtException(JwtResult.INVALID_PERIOD);
        }
        cookie.setMaxAge((int) refreshTokenProvider.tokenValidityInMilliseconds);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setSecure(true);
        response.addCookie(cookie);

    }

    /**
     * RefreshToken DB 저장(업데이트)
     */
    @Transactional
    public void updateRefreshToken(String email, String refreshToken) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberResult.NOT_FOUND_MEMBER));
        member.updateRefreshToken(refreshToken);
    }


    /**
     * 로그아웃 시 쿠키 만료를 통한 Refresh Token 만료.
     *
     * @param response
     * @Param request
     */
    public void expireRefreshToken(HttpServletResponse response, HttpServletRequest request) {
        Cookie refreshCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(REFRESH_HEADER))
                .findFirst()
                .orElse(null);
        if (refreshCookie == null) {
            throw new MemberException(MemberResult.NOT_FOUND_MEMBER);
        }
        refreshCookie.setMaxAge(0);
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);
    }


    public void refresh(HttpServletResponse response, Authentication authentication, String refreshToken) {

        // === Refresh Token 유효성 검사 === //
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);

        // === Access Token 재발급 === //
        long now = System.currentTimeMillis();
        String email = decodedJWT.getSubject();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberResult.NOT_FOUND_MEMBER));
        if (!member.getRefreshToken().equals(refreshToken)) {
            throw new JwtException(JwtResult.INVALID_REFRESH_TOKEN);
        }
        String accessToken = createAccessToken(authentication);

        // === 현재시간과 Refresh Token 만료날짜를 통해 남은 만료기간 계산 === //
        // === Refresh Token 만료시간 계산해 5분 미만일 시 refresh token도 발급 === //
        long refreshExpireTime = decodedJWT.getClaim("exp").asLong() * 1000;
        long diffMin = (refreshExpireTime - now) / 1000 / 60;
        if (diffMin < 5) {
            String newRefreshToken = createRefreshToken(authentication);
            updateRefreshToken(email, newRefreshToken);
            sendAccessAndRefreshToken(response, accessToken, newRefreshToken);

        }
        setAccessTokenHeader(response, accessToken);

    }
}

