package com.ll.ShinChekBang.boundedContext.order.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.cart.entity.CartBook;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.entity.OrderBook;
import com.ll.ShinChekBang.boundedContext.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Transactional
    public RsData<Order> order(Member member) {
        Cart cart = member.getCart();

        Order order = Order.builder()
                .member(member)
                .paymentAccount(cart.getTotalPrice())
                .build();
        if (canOrder(cart, order)) {
            Order completeOrder = orderRepository.save(order);
            return RsData.of("S-6", "주문이 성공적으로 처리되었습니다.", completeOrder);
        }
        return RsData.of("F-5", "주문처리 도중 문제가 일어났습니다.");
    }

    private boolean canOrder(Cart cart, Order order) {
        for (CartBook cartBook : cart.getCartBooks()) {
            Long bookId = cartBook.getBook().getId();
            int quantity = cartBook.getQuantity();

            Optional<Book> optionalBook = bookRepository.findById(bookId);
            if (optionalBook.isEmpty()) {
                return false;
            }
            Book book = optionalBook.get();
            if (book.getStock() < quantity) {
                return false;
            }

            order.getOrderItems().add(
                    OrderBook.builder()
                            .order(order)
                            .book(book)
                            .quantity(quantity)
                            .build());
        }
        return true;
    }
}
