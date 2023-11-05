package com.milkcow.tripai.member.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginRequestDto {

    private  String email;

    private  String pw;
}
