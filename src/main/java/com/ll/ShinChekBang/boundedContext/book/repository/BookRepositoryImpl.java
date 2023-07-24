package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.entity.QBook;
import com.ll.ShinChekBang.boundedContext.order.entity.QOrder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QOrder order = QOrder.order;
    private final QBook book = QBook.book;
    @Override
    public List<Book> findTop100InPastYear() {
        List<Book> topBooks = queryFactory
                .select(book)
                .from(order).join(order.books, book)
                .where(order.createDate.after(LocalDateTime.now().minusDays(7)))
                .groupBy(book.id)
                .orderBy(Expressions.numberTemplate(Long.class, "count({0})", book.id).desc())
                .limit(100)
                .fetch();
        return topBooks;
    }
}
