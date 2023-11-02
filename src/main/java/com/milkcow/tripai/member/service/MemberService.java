package com.milkcow.tripai.member.service;


import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.dto.MemberSignupRequestDto;
import com.milkcow.tripai.member.dto.MemberUpdateRequestDto;
import com.milkcow.tripai.member.dto.MemberWithdrawRequestDto;
import com.milkcow.tripai.member.exception.MemberException;
import com.milkcow.tripai.member.repository.MemberRepository;
import com.milkcow.tripai.member.result.MemberResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;


    public Member emailSignUp(MemberSignupRequestDto requestDto){

        //중복 회원 검사 (email / nickname)
        if (isDuplicated(requestDto.getEmail(), requestDto.getNickname())) {
            throw new MemberException(MemberResult.ALREADY_EXIST_USER_EMAIL);
        }
        Member member = requestDto.toEntity();
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public void updateNickname(Long memberId, MemberUpdateRequestDto requestDto){

        if(requestDto.getPw() == null){
            throw new MemberException(MemberResult.INVALID_INPUT);
        }
        Member member = memberRepository.findById(memberId).get();

        if (!member.getPassword().matches(requestDto.getPw())) {
            throw new MemberException(MemberResult.INVALID_PASSWORD);
        }
        requestDto.getNickname().ifPresent(member::updateNickname);
    }

    public void withdraw(Long memberId, MemberWithdrawRequestDto requestDto){

        if(requestDto.getPw() == null){
            throw new MemberException(MemberResult.INVALID_INPUT);
        }
        Member member = memberRepository.findById(memberId).get();

        if (!member.getPassword().matches(requestDto.getPw())) {
            throw new MemberException(MemberResult.INVALID_PASSWORD);
        }

        memberRepository.delete(member);

    }

//
//    public Member myPage(){
//
//    }

    private boolean isDuplicated(String email, String nickname) {
        return memberRepository.existsByEmailAndNickname(email, nickname);

    }
}
