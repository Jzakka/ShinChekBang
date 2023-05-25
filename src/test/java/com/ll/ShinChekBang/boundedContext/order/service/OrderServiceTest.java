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

import java.util.List;

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
        cartService.addToCart(user1.getCart(), book1);
        cartService.addToCart(user1.getCart(), book2);
    }

    @Test
    void 장바구니에_담은_물건_주문() {
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        Book book2 = bookService.findByTitle("책2").getData().get(0);
        List<Book> orderBooks = List.of(book1, book2);
        Member member = memberRepository.findByUsername("user1").get();

        RsData<Order> orderResult = orderService.order(member, orderBooks, 25000);

        assertThat(orderResult.isSuccess()).isTrue();
        assertThat(orderResult.getData().getPaymentAccount()).isEqualTo(25000);
    }
}