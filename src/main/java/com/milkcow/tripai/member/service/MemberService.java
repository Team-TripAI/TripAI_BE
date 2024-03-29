package com.milkcow.tripai.member.service;


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
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    /**
     * 이메일 회원가입
     *
     * @param requestDto
     * @return 저장된 member
     * @throws MemberException 중복회원 가입시 예외
     */

    public Member emailSignUp(MemberSignupRequestDto requestDto) {

        if (isDuplicatedEmail(requestDto.getEmail()) || isDuplicatedNickname(requestDto.getNickname())) {
            throw new MemberException(MemberResult.ALREADY_EXIST_USER);
        }
        Member member = requestDto.toEntity();
        memberRepository.save(member);
        return member;
    }

    /**
     * 닉네임 변경
     *
     * @param memberId
     * @param requestDto
     */
    public void updateNickname(Long memberId, MemberUpdateRequestDto requestDto) {

        Member member = memberRepository.findById(memberId).get();

        if (!member.getPassword().matches(requestDto.getPw())) {
            throw new MemberException(MemberResult.INVALID_PASSWORD);
        }
        if (isDuplicatedNickname(String.valueOf(requestDto.getNickname()))) {
            throw new MemberException(MemberResult.ALREADY_EXIST_USER);
        }
        requestDto.getNickname().ifPresent(member::updateNickname);
    }

    /**
     * 회원 탈퇴
     *
     * @param memberId
     * @param requestDto
     */
    public void withdraw(Long memberId, MemberWithdrawRequestDto requestDto) {

        Member member = memberRepository.findById(memberId).get();

        if (!member.getPassword().matches(requestDto.getPw())) {
            throw new MemberException(MemberResult.INVALID_PASSWORD);
        }

        memberRepository.delete(member);

    }

    public Member getMemberByRefreshToken(String refreshToken) {

        return memberRepository.findByRefreshToken(refreshToken);
    }


    /**
     * 이메일 중복검사
     *
     * @param email
     * @return true : 중복 존재
     * @return false : 중복 없음
     */
    private boolean isDuplicatedEmail(String email) {

        return memberRepository.existsByEmail(email);

    }

    /**
     * 닉네임 중복검사
     *
     * @param nickname
     * @return true : 중복 존재
     * @return false : 중복 없음
     */
    private boolean isDuplicatedNickname(String nickname) {

        return memberRepository.existsByNickname(nickname);

    }
}
