package com.ll.ShinChekBang.boundedContext.cart.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CartServiceTest {
    @Autowired
    CartService cartService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BookRepository bookRepository;

    @Test
    void 회원생성시_장바구니도_생성() {
        Member user1 = memberRepository.findByUsername("user1").get();
        assertThat(user1.getCart()).isNotNull();
    }

    @Test
    void 빈_장바구니에_물건담기() {
        Member user1 = memberRepository.findByUsername("user1").get();
        Book book1 = bookRepository.findByTitle("책1").get(0);
        Cart cart = user1.getCart();

        RsData<Cart> addResult = cartService.addToCart(cart, book1, 5);
        assertThat(addResult.isSuccess()).isTrue();
        assertThat(addResult.getData().getCartBooks().get(0).getPriceSum()).isEqualTo(book1.getPrice() * 5);
    }

    @Test
    void 재고없는_물건_담기는_안돼() {
        Member user1 = memberRepository.findByUsername("user1").get();
        Book book3 = bookRepository.findByTitle("책3").get(0);
        Cart cart = user1.getCart();

        RsData<Cart> addResult = cartService.addToCart(cart, book3, 5);
        assertThat(addResult.isFail()).isTrue();
    }

    @Test
    void 이미_담긴_물건에_더_담기() {
        Member user1 = memberRepository.findByUsername("user1").get();
        Book book1 = bookRepository.findByTitle("책1").get(0);
        Book book2 = bookRepository.findByTitle("책2").get(0);
        Cart cart = user1.getCart();

        cartService.addToCart(cart, book1, 5);
        cartService.addToCart(cart, book2, 5);
        RsData<Cart> additional = cartService.addToCart(cart, book1, 5);
        assertThat(additional.isSuccess()).isTrue();
        assertThat(additional.getData().getCartBooks().get(0).getQuantity()).isEqualTo(10);
        assertThat(additional.getData().getTotalPrice()).isEqualTo(book1.getPrice() * 10 + book2.getPrice() * 5);
    }
}