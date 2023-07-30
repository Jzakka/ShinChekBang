package com.ll.ShinChekBang.boundedContext.order.temporary;

import com.ll.ShinChekBang.base.file.entity.UploadFile;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class Receipt {
    private String orderId;
    private String orderedBooks;
    private String thumbnail;
    private String orderName;
    private Integer paymentAmount;

    public Receipt(Order order) {
        orderId = order.getOrderId();
        orderName = makeOrderName(order.getBooks());
        orderedBooks = order.getBooks().stream().map(book -> book.getTitle()+"\n").collect(Collectors.joining());
        thumbnail = Optional.ofNullable(order.getBooks().get(0).getThumbnail())
                .map(UploadFile::getStoreFileName)
                .orElse(null);
        paymentAmount = order.getPaymentAccount();
    }

    private String makeOrderName(List<Book> books) {
        if (books.size() == 1) {
            return books.get(0).getTitle();
        }
        return books.get(0).getTitle() + " 외 " + (books.size() - 1) + "건";
    }
}
