package com.ll.ShinChekBang.boundedContext.book.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BookServiceTest {
    @Autowired
    BookService bookService;

    @Test
    void 책등록() throws IOException {
        RsData<Book> result = bookService.addNewBook("책제목", "글쓴이", 15000, null, null);
        assertThat(result.isSuccess()).isTrue();
    }
}