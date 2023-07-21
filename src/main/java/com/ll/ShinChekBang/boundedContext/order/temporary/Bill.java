package com.ll.ShinChekBang.boundedContext.order.temporary;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@RedisHash("Bill")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class BookVO {
        private long id;
        private String title;
        private int price;
    }

    @Id
    private String id;
    private List<BookVO> books;
    private int paymentAmount;
    private String orderName;

    public Bill( List<Book> books, int paymentAmount) {
        this.id = UUID.randomUUID().toString();
        this.books = books.stream()
                .map(book -> new Bill.BookVO(book.getId(), book.getTitle(), book.getPrice()))
                .toList();
        this.paymentAmount = paymentAmount;
        this.orderName = createOrderName();
    }

    private String createOrderName() {
        if (books.size() > 3) {
            return "%s 외 %d개".formatted(books.get(0).getTitle(), books.size() - 1);
        }
        return books.stream().map(BookVO::getTitle).collect(Collectors.joining(",\n"));
    }
}
