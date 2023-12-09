package com.milkcow.tripai.member.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.milkcow.tripai.global.dto.ResponseDto;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.jwt.JwtService;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.dto.MemberSignupRequestDto;
import com.milkcow.tripai.member.exception.OAuth2Exception;
import com.milkcow.tripai.member.repository.MemberRepository;
import com.milkcow.tripai.member.result.OAuth2Result;
import com.milkcow.tripai.security.MemberAdapter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OAuth2Service {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final MemberService memberService;
    private static final NetHttpTransport transport = new NetHttpTransport();
    private static final JacksonFactory jsonFactory = new JacksonFactory();
    @Value("${OAuth2.Google-Client-Id}")
    private String clientId;
    private final GoogleIdTokenVerifier verifier =
            new Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList(clientId))
            .build();

    /**
     * google OAuth2 로그인 진행 회원가입 되어있지 않은 경우 회원가입 진행
     *
     * @param token    (String, accessToken)
     * @param response (ResponseDto)
     */
    @Transactional
    public ResponseDto oAuth2Login(String token, HttpServletResponse response) {
        GoogleData googleData = getGoogleDataFromToken(token);
        String email = googleData.getEmail();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        // 회원가입 진행
        if (optionalMember.isEmpty()) {
            MemberSignupRequestDto signupDto = MemberSignupRequestDto.builder()
                    .email(email)
                    .pw(UUID.randomUUID().toString().replaceAll("-", ""))
                    .nickname(googleData.getNickname())
                    .build();

            Member member = memberService.emailSignUp(signupDto);
            MemberAdapter memberAdapter = new MemberAdapter(member);
            sendAccessAndRefreshToken(response, memberAdapter);
        } else {
            Member member = optionalMember.get();
            MemberAdapter memberAdapter = new MemberAdapter(member);
            sendAccessAndRefreshToken(response, memberAdapter);
        }
        return ResponseDto.of(true, OAuth2Result.OK_GOOGLE_LOGIN);
    }

    private void sendAccessAndRefreshToken(HttpServletResponse response, MemberAdapter memberAdapter) {

        String accessToken = jwtService.createAccessToken(memberAdapter.getUsername(),
                memberAdapter.getMember().getId());
        String refreshToken = jwtService.createRefreshToken(memberAdapter.getUsername());

        jwtService.updateRefreshToken(memberAdapter.getMember().getEmail(), refreshToken);

        // 2. header에 JWT토큰을 넣어서 응답
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    }

    /**
     * 프론트엔드로 부터 받은 Google OAuth2 credential 바탕으로 회원 정보 추출
     *
     * @param token (프론트엔드로 부터 받은 Google OAuth2 credential)
     * @return GoogleDataDto
     */
    private GoogleData getGoogleDataFromToken(String token) {
        try{
//            GoogleIdToken idToken = verifier.verify(token);
            GoogleIdToken idToken = GoogleIdToken.parse(new JacksonFactory(), token);

            if(idToken == null){
                throw new OAuth2Exception(OAuth2Result.INVALID_ACCESS_TOKEN);
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            String nickname = (String) payload.get("name");
            String email = payload.getEmail();

            return new GoogleData(email, nickname);
        } catch (IOException e) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e){
            throw new OAuth2Exception(OAuth2Result.INVALID_ACCESS_TOKEN);
        }
    }

    @Data
    private class GoogleData {
        public String email;
        public String nickname;

        public GoogleData(String email, String nickname) {
            this.email = email;
            this.nickname = nickname;
        }
    }
}
