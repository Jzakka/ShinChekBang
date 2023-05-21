package com.ll.ShinChekBang.boundedContext.member.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.cart.repository.CartRepository;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

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

        String cryptPassword = passwordEncoder.encode(password);

        Member newMember = Member.builder()
                .username(username)
                .password(cryptPassword)
                .email(email)
                .build();
        Cart cart = Cart.builder()
                .member(newMember)
                .build();
        newMember.setCart(cart);

        Member joinedMember = memberRepository.save(newMember);
        cartRepository.save(cart);

        return RsData.of("S-1", "회원가입되었습니다.", joinedMember);
    }

    public RsData<Member> findByUsername(String name) {
        Optional<Member> optionalMember = memberRepository.findByUsername(name);
        return optionalMember.map(RsData::successOf).orElseGet(() -> RsData.failOf(null));
    }
}
