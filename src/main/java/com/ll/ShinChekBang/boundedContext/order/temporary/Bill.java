package com.ll.ShinChekBang.boundedContext.order.temporary;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

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
    private Long memberId;
    private List<BookVO> books;
    private int paymentAmount;

    public Bill(Member member, List<Book> books, int paymentAmount) {
        this.memberId = member.getId();
        this.books = books.stream()
                .map(book -> new Bill.BookVO(book.getId(), book.getTitle(), book.getPrice()))
                .toList();
        this.paymentAmount = paymentAmount;
    }


}
