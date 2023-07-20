package com.ll.ShinChekBang.boundedContext.order.vo;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Receipt {
    @AllArgsConstructor
    @Getter
    public static class BookVO{
        private long id;
        private String title;
        private int price;
    }

    private List<BookVO> books;
    private int paymentAmount;

    public Receipt(List<Book> books, int paymentAmount) {
        this.books = books.stream()
                .map(book -> new Receipt.BookVO(book.getId(), book.getTitle(), book.getPrice()))
                .toList();
        this.paymentAmount = paymentAmount;
    }
}
