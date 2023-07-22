package com.ll.ShinChekBang.boundedContext.order.temporary;

import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import lombok.Getter;

import java.util.stream.Collectors;

@Getter
public class Receipt {
    private String orderId;
    private String orderedBooks;
    private Integer paymentAmount;

    public Receipt(Order order) {
        orderId = order.getOrderId();
        orderedBooks = order.getBooks().stream().map(book -> book.getTitle()+"\n").collect(Collectors.joining());
        paymentAmount = order.getPaymentAccount();
    }
}
