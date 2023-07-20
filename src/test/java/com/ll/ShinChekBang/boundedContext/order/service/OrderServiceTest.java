package com.ll.ShinChekBang.boundedContext.order.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.cart.service.CartService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.vo.Receipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("계산서 만들기")
    void makeReceipt() {
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        Book book2 = bookService.findByTitle("책2").getData().get(0);
        List<Book> orderBooks = List.of(book1, book2);

        Receipt receipt = orderService.makeReceipt(orderBooks);

        int paymentAmount = book1.getPrice() + book2.getPrice();

        assertThat(receipt).isNotNull();
        assertThat(receipt.getBooks().get(0).getTitle()).isEqualTo("책1");
        assertThat(receipt.getBooks().get(1).getTitle()).isEqualTo("책2");
        assertThat(receipt.getPaymentAmount()).isEqualTo(paymentAmount);
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