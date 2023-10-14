package com.milkcow.tripai.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.global.dto.ResponseDto;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.dto.GoogleDataDto;
import com.milkcow.tripai.member.exception.OAuth2Exception;
import com.milkcow.tripai.member.repository.MemberRepository;
import com.milkcow.tripai.member.result.OAuth2Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OAuth2Service {
    private final MemberRepository memberRepository;

    /**
     * google OAuth2 로그인 진행
     * 회원가입 되어있지 않은 경우 회원가입 진행
     * @param token (String, accessToken)
     * @param response (ResponseDto)
     */
    public ResponseDto oAuth2Login(String token, HttpServletResponse response){
        GoogleDataDto googleData = getGoogleData(token);
        String email = googleData.getEmail();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        // 회원가입 진행
        if(optionalMember.isEmpty()){
            Member member = Member.builder()
                    .email(email)
                    .nickname(googleData.getNickname())
                    .password(UUID.randomUUID().toString().replaceAll("-", ""))
                    .build();
            memberRepository.save(member);

            //해당 유저에 대한 JWT 발급 절차 진행
            System.out.println("이메일: " + member.getEmail());
            System.out.println("회원가입 및 JWT발급");
        }
        else {
            Member member = optionalMember.get();
            //해당 유저에 대한 JWT 발급 절차 진행
            System.out.println("이메일: " + member.getEmail());
            System.out.println("로그인 및 JWT발급");
        }
        return ResponseDto.of(true, OAuth2Result.OK_GOOGLE_LOGIN);
    }

    /**
     * getGoogleData<p>
     * 프론트엔드로 부터 받은 Google OAuth2 access_token을 바탕으로 회원 정보 조회.
     * @param token (프론트엔드로 부터 받은 Google OAuth2 access_token)
     * @return GoogleDataDto
     */
    private GoogleDataDto getGoogleData(String token) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            //구글에 정보 요청
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                    "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
                            + token,
                    String.class
            );
            HttpStatus statusCode = responseEntity.getStatusCode();

            // 유효하지 않은 토큰인 경우
            if(statusCode.is4xxClientError()){
                throw new OAuth2Exception(OAuth2Result.INVALID_ACCESS_TOKEN);
            }

            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            //response body로 부터 정보 추출
            String nickname = jsonNode.get("name").asText();
            String email = jsonNode.get("email").asText();
            return new GoogleDataDto(nickname, email);
        } catch (RestClientException e){
            throw new OAuth2Exception(OAuth2Result.FAIL_TO_ACCESS_GOOGLE_API);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        }
    }
}
