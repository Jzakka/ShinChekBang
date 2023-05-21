package com.ll.ShinChekBang.boundedContext.review.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import com.ll.ShinChekBang.boundedContext.review.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReviewServiceTest {
    @Autowired
    ReviewService reviewService;
    @Autowired
    MemberService memberService;
    @Autowired
    BookService bookService;

    @Test
    void 리뷰작성_성공() {
        Member member2 = memberService.findByUsername("user2").getData();
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        RsData<Review> reviewResult = reviewService.review(member2, book1, 4.6f, "너무 재밌어요");

        assertThat(reviewResult.isSuccess()).isTrue();
    }

    @Test
    void 리뷰작성_실패() {
        Member member2 = memberService.findByUsername("user2").getData();
        Book book2 = bookService.findByTitle("책2").getData().get(0);
        RsData<Review> reviewRsData = reviewService.review(member2, book2, 2.3f, "형편없는 책이에요");

        assertThat(reviewRsData.isFail()).isTrue();
    }
}