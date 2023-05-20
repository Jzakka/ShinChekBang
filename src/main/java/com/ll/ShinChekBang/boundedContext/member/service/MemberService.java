package com.ll.ShinChekBang.boundedContext.member.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public RsData<Member> join(String username, String password, String passwordConfirm, String email) {
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return RsData.of("F-1", "이미 존재하는 이름입니다.");
        }

        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return RsData.of("F-2", "이미 가입한 이메일입니다.");
        }

        if (!password.equals(passwordConfirm)) {
            return RsData.of("F-3", "비밀번호를 다시 확인해주세요.");
        }

        Member newMember = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
        Member joinedMember = memberRepository.save(newMember);
        return RsData.of("S-1", "회원가입되었습니다.", joinedMember);
    }
}
