package com.milkcow.tripai.member.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String email;

    @Embedded
    private Password password;

    @NonNull
    private String nickname;

    private String refreshToken;



    @Builder
    public Member(Long id, String email, Password password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public Member(String email, Password password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static Member self(final String email, final String password, final String nickname) {
        return new Member(email, new Password(password), nickname);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String updateNickname){
        this.nickname = updateNickname;
    }
}