package com.ll.ShinChekBang.boundedContext.order.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.cart.service.CartService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;
    @Autowired
    BookService bookService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void 장바구니에_물건담기() {
        Member user1 = memberRepository.findByUsername("user1").get();
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        Book book2 = bookService.findByTitle("책2").getData().get(0);
        cartService.addToCart(user1.getCart(), book1, 10);
        cartService.addToCart(user1.getCart(), book2, 5);
    }

    @Test
    void 장바구니에_담은_물건_주문() {
        Member user1 = memberRepository.findByUsername("user1").get();
        RsData<Order> orderResult = orderService.order(user1);
        assertThat(orderResult.isSuccess()).isTrue();
        Order order = orderResult.getData();
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        Book book2 = bookService.findByTitle("책2").getData().get(0);
        assertThat(order.getPaymentAccount()).isEqualTo(book1.getPrice() * 10 + book2.getPrice() * 5);
    }

    @Test
    void 주문도중_재고가_줄어서_주문실패() {
        Member user1 = memberRepository.findByUsername("user1").get();
        Book book2 = bookService.findByTitle("책2").getData().get(0);

        //재고를 줄임
        bookService.adjustStock(book2, 0);

        RsData<Order> orderResult = orderService.order(user1);
        assertThat(orderResult.isFail()).isTrue();
    }
}