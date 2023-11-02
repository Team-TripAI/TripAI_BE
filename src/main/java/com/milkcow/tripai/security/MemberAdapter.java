package com.milkcow.tripai.security;

import com.milkcow.tripai.member.domain.Member;

public class MemberAdapter extends CustomUserDetails {
    private final Member member;

    public MemberAdapter(Member member) {
        super(member);
        this.member = member;
    }
}