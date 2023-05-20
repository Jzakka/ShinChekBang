package com.ll.ShinChekBang.boundedContext.book.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BookServiceTest {
    @Autowired
    BookService bookService;

    @Test
    void 책등록() {
        RsData<Book> result = bookService.addNewBook("책제목", "글쓴이", 15000);
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void 책_재고_증가() {
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        Book book = bookService.store(book1, 100).getData();
        assertThat(book.getStock()).isEqualTo(100);
    }
}