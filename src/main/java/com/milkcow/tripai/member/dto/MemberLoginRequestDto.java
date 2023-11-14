package com.milkcow.tripai.member.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginRequestDto {

    @ApiParam(value = "사용자 이메일")
    @ApiModelProperty(example = "test@gmail.com")
    private String email;

    @ApiParam(value = "사용자 비밀번호")
    @ApiModelProperty(example = "test!1234")
    private String pw;
}
