package com.milkcow.tripai.member.result;

import com.milkcow.tripai.global.result.ResultProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberResult implements ResultProvider {

    //회원가입 시
    OK_SIGNUP(200, HttpStatus.OK, "회원가입 성공!"),
    OK_NICKNAME_UPDATE(200, HttpStatus.OK, "닉네임 수정 성공!"),

    OK_WITHDRAW(200, HttpStatus.OK, "회원탈퇴 성공!"),

    ALREADY_EXIST_USER(419, HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),

    INVALID_INPUT(420, HttpStatus.EXPECTATION_FAILED, "입력이 잘못되었습니다."),


    INVALID_PASSWORD(430, HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다."),

    //로그인 실패 시
    LOGIN_FAILURE(401, HttpStatus.UNAUTHORIZED, "등록되지 않은 이메일 또는 비밀번호를 잘못 입력했습니다."),

    //회원을 찾을 수 없는 경우
    NOT_FOUND_MEMBER(404, HttpStatus.NOT_FOUND, "회원 정보가 없습니다."),


    INVALIDATE_REFRESH_TOKEN(489, HttpStatus.UNAUTHORIZED, "만료 또는 유효하지 않은 REFRESH 토큰입니다.");

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
