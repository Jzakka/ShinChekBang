package com.ll.ShinChekBang.boundedContext.member.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    BookService bookService;

    @Test
    void 회원가입_테스트_성공() {
        RsData<Member> result = memberService.join("개똥이", "1234", "1234", "dogPoo@test.com");
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void 회원가입_테스트_실패_중복이름() {
        memberService.join("개똥이", "1234", "1234", "dogPoo@test.com");
        RsData<Member> result = memberService.join("개똥이", "1234", "1234", "dogPoof@test.com");
        assertThat(result.isFail()).isTrue();
        assertThat(result.getMsg()).contains("이미 존재하는 이름입니다.");
    }

    @Test
    void 회원가입_테스트_실패_중목이메일() {
        memberService.join("개똥이", "1234", "1234", "dogPoo@test.com");
        RsData<Member> result = memberService.join("말똥이", "1234", "1234", "dogPoo@test.com");
        assertThat(result.isFail()).isTrue();
        assertThat(result.getMsg()).contains("이미 가입한 이메일입니다.");
    }

    @Test
    void 회원가입_테스트_실패_비밀번호확인_불일치() {
        RsData<Member> result = memberService.join("개똥이", "1234", "1235", "dogPoo@test.com");
        assertThat(result.isFail()).isTrue();
        assertThat(result.getMsg()).contains("비밀번호를 다시 확인해주세요.");
    }
}