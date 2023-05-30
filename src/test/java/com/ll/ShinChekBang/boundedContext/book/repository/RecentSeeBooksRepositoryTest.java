package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.base.entity.BaseEntity;
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

    @Test
    void _20개_이상_컷() {
        Long userId = 1L;
        for (long i = 1L; i <= 30; i++) {
            Book book = Book.builder().id(i).build();
            repository.saveBook(userId, book);
        }
        List<Book> books = repository.getBooks(userId);
        Assertions.assertThat(books.stream().mapToLong(BaseEntity::getId))
                .containsExactly(
                        30L,29L,28L,27L,26L,
                        25L,24L,23L,22L,21L,
                        20L,19L,18L,17L,16L,
                        15L,14L,13L,12L,11L);
    }
}