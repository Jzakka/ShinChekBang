package com.ll.ShinChekBang.boundedContext.order.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.repository.MemberRepository;
import com.ll.ShinChekBang.boundedContext.order.vo.Receipt;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

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
    public RsData<Order> order(Member member, Receipt receipt) {
        List<Long> bookIds = receipt.getBooks().stream().map(Receipt.BookVO::getId).toList();
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

    public Receipt makeReceipt(List<Book> books) {
        // 추후에 할인정책에 따라 결제총액 계산방법이 변경될 수도 있음
        int paymentAmount = books.stream().mapToInt(Book::getPrice).sum();
        Receipt receipt = new Receipt(books, paymentAmount);

        return receipt;
    }
}
