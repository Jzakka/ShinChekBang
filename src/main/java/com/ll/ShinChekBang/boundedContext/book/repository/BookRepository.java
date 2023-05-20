package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
}
