package com.milkcow.tripai.member.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberUpdateRequestDto {

    @ApiParam(value = "사용자 닉네임")
    @ApiModelProperty(example = "jeonghwan")
    private Optional<String> nickname;

    @ApiParam(value = "사용자 비밀번호")
    @ApiModelProperty(example = "test!1234")
    private String pw;

}
