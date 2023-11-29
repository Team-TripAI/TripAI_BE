package com.milkcow.tripai.member.dto;

import com.milkcow.tripai.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberFindResponseDto {

    @Schema(description = "회원 닉네임", example = "sooyoung")
    private final String nickname;

    public static MemberFindResponseDto from(Member member) {
        return new MemberFindResponseDto(member.getNickname());
    }
}
