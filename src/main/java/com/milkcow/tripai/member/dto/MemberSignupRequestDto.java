package com.milkcow.tripai.member.dto;

import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.domain.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSignupRequestDto {

    private  String email;

    private  String pw;

    private  String nickname;

    @Builder
    public MemberSignupRequestDto(String email, String pw, String nickname) {
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
    }

    public Member toEntity(){
        return Member.self(email, pw,nickname);

    }

}
