package com.ll.ShinChekBang.boundedContext.cart.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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

        RsData<Cart> addResult = cartService.addToCart(cart, book1);
        assertThat(addResult.isSuccess()).isTrue();
        assertThat(cart.getTotalPrice()).isEqualTo(book1.getPrice());
    }

    @Test
    void 이미_담긴_물건에_더_담기는_실패() {
        Member user1 = memberRepository.findByUsername("user1").get();
        Book book1 = bookRepository.findByTitle("책1").get(0);
        Cart cart = user1.getCart();

        cartService.addToCart(cart, book1);
        RsData<Cart> additional = cartService.addToCart(cart, book1);
        assertThat(additional.isFail()).isTrue();
    }

    @Test
    void 이미_갖고있는_책_담기는_실패() {
        Member user1 = memberRepository.findByUsername("user1").get();
        Book book1 = bookRepository.findByTitle("책1").get(0);
        user1.getBooks().add(book1);
        Cart cart = user1.getCart();

        RsData<Cart> addToCart = cartService.addToCart(cart, book1);
        assertThat(addToCart.isFail()).isTrue();
    }
}