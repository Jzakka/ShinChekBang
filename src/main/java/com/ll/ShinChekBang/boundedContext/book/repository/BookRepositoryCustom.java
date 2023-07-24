package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findTop100InPastYear();
}
