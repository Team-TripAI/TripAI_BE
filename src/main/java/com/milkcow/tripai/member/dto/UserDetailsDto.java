package com.milkcow.tripai.member.dto;

import io.swagger.annotations.ApiParam;
import java.util.Collection;
import lombok.Data;
import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
public class UserDetailsDto implements UserDetails {

    @Delegate
    private MemberSignupRequestDto requestDto;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    @ApiParam(value = "비밀번호를 반환해준다.")
    public String getPassword() {
        return requestDto.getPw();
    }


    @Override
    @ApiParam(value = "이메일을 반환해준다.")
    public String getUsername() {
        return requestDto.getEmail();
    }

    @Override
    @ApiParam(value = "계정 만료 여부 true :  만료 안됨 false : 만료")
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @ApiParam(value = "계정 잠김 여부 true : 잠기지 않음 false : 잠김")
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @ApiParam(value = "비밀번호 만료 여부 true : 만료 안 됨 false : 만료")
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @ApiParam(value = "사용자 활성화 여부 true : 활성화 됨 false : 활성화 안 됨")
    public boolean isEnabled() {
        return false;
    }
}
