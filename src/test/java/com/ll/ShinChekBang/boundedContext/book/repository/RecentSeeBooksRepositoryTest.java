package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecentSeeBooksRepositoryTest {
    @Autowired
    RecentSeeBooksRepository repository;

    @AfterEach
    void 데이터_리셋() {
        repository.clear(1L);
    }

    @Test
    void 저장_조회_성공() {
        Long userId = 1L;
        Book book1 = Book.builder()
                .title("책1")
                .build();
        repository.saveBook(userId, book1);
        List<Book> books = repository.getBooks(userId);
        Assertions.assertThat(books).containsExactly(book1);
    }
}