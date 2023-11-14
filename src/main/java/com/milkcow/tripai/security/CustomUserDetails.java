package com.milkcow.tripai.security;

import com.milkcow.tripai.member.domain.Member;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    /**
     * 유저의 권한 목록, 권한 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList((GrantedAuthority) () -> "ROLE_USER");
    }


    public Member getMember() {
        return member;
    }

    @Override
    public String getPassword() {
        return member.getPassword().toString();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    /**
     * 계정 만료 여부 true :  만료 안됨 false : 만료
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부 true : 잠기지 않음 false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호 만료 여부 true : 만료 안 됨 false : 만료
     */

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자 활성화 여부 true : 활성화 됨 false : 활성화 안 됨
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
