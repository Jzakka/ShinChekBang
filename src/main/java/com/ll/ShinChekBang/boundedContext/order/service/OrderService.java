package com.ll.ShinChekBang.boundedContext.order.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public RsData<Order> order(Member member) {
        Cart cart = member.getCart();

        Order order = Order.builder()
                .member(member)
                .paymentAccount(cart.getTotalPrice())
                .build();
        Order completeOrder = orderRepository.save(order);

        return RsData.of("S-6", "주문이 성공적으로 처리되었습니다.", completeOrder);
    }
}
