package com.milkcow.tripai.member.dto;

import com.milkcow.tripai.member.domain.Member;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSignupRequestDto {

    @ApiParam(value = "사용자 이메일")
    @ApiModelProperty(example = "test@gmail.com")
    private String email;

    @ApiParam(value = "사용자 비밀번호")
    @ApiModelProperty(example = "test!1234")
    private String pw;

    @ApiParam(value = "사용자 닉네임")
    @ApiModelProperty(example = "jeonghwan")
    private String nickname;

    @Builder
    public MemberSignupRequestDto(String email, String pw, String nickname) {
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
    }

    public Member toEntity() {
        return Member.self(email, pw, nickname);

    }

}
