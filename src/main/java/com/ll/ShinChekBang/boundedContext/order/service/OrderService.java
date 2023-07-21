package com.ll.ShinChekBang.boundedContext.order.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import com.ll.ShinChekBang.boundedContext.order.repository.BillRepository;
import com.ll.ShinChekBang.boundedContext.order.temporary.Bill;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BillRepository billRepository;
    private final RestTemplate restTemplate;
    private final String TOSS_PG_URL = "https://api.tosspayments.com/v1/payments/";

    @Transactional
    public RsData<Order> order(Member member, List<Book> orderBooks, int paymentAmount) {
        Order order = Order.builder()
                .member(member)
                .books(orderBooks)
                .paymentAccount(paymentAmount)
                .build();
        member.getOrders().add(order);
        memberRepository.save(member);
        memberRepository.flush();

        return RsData.of("S-12", "주문을 생성했습니다.", order);
    }

    @Transactional
    public RsData<Order> order(Member member, Bill receipt) {
        List<Long> bookIds = receipt.getBooks().stream().map(Bill.BookVO::getId).toList();
        List<Book> books = bookRepository.findAllById(bookIds);

        Order order = Order.builder()
                .member(member)
                .books(books)
                .paymentAccount(receipt.getPaymentAmount())
                .build();
        member.getOrders().add(order);
        memberRepository.save(member);
        memberRepository.flush();

        return RsData.of("S-12", "주문을 생성했습니다.", order);
    }

    public Bill makeBill(Member member, List<Book> books) {
        // 추후에 할인정책에 따라 결제총액 계산방법이 변경될 수도 있음
        int paymentAmount = books.stream().mapToInt(Book::getPrice).sum();
        Bill bill = new Bill(member, books, paymentAmount);
        billRepository.save(bill);

        return bill;
    }

    @Transactional
    public RsData<Order> pay(Member member, String paymentId, int amount, String paymentKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> data = new HttpEntity<>("""
                {
                    "orderId":"%s",
                    "amount":%d
                }
                """.formatted(paymentId, amount), headers);
        ResponseEntity<Object> paymentValidation = restTemplate.exchange(TOSS_PG_URL + paymentKey,
                HttpMethod.POST,
                data,
                new ParameterizedTypeReference<>() {}
        );

        if (paymentValidation.getStatusCode().is2xxSuccessful()) {
            // 주문을 생성하고 반환
        }

        return RsData.of("F-20", "잘못된 결제요청입니다.");
    }
}
