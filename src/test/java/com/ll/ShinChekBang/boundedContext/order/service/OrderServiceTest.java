package com.ll.ShinChekBang.boundedContext.order.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.cart.service.CartService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.temporary.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @MockBean
    RestTemplate restTemplate;
    @Autowired
    CartService cartService;
    @Autowired
    BookService bookService;
    @Autowired
    MemberRepository memberRepository;

    Member member1;

    @BeforeEach
    void 장바구니에_물건담기() {
        member1 = memberRepository.findByUsername("user1").get();
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        Book book2 = bookService.findByTitle("책2").getData().get(0);
        cartService.addToCart(member1.getCart(), book1);
        cartService.addToCart(member1.getCart(), book2);
    }

    @Test
    @DisplayName("계산서 만들기")
    void makeReceipt() {
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        Book book2 = bookService.findByTitle("책2").getData().get(0);
        List<Book> orderBooks = List.of(book1, book2);

        Bill receipt = orderService.makeBill(orderBooks);

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

    @Test
    @DisplayName("PG결제 테스트, 주문 정보가 없음")
    void pay() {
        Mockito
                .when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("dummy response", HttpStatus.OK));

        assertThatThrownBy(() -> orderService.pay(member1, "dummyId", 1000, "dummyKey"))
                .isInstanceOf(ResponseStatusException.class);
    }
}