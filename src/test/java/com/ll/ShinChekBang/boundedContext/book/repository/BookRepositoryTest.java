package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("지난 일주일간 판매량 TOP 100")
    void top100LastWeek() {
        List<Book> top100InPastYear = bookRepository.findTop100InPastWeek();
//        top100InPastYear.forEach(System.out::println);
        assertThat(top100InPastYear.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("지난 한달간 판매량 TOP 100")
    void top100LastMonth() {
        List<Book> top100InPastYear = bookRepository.findTop100InPastMonth();
        assertThat(top100InPastYear.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("지난 일년간 판매량 TOP 100")
    void top100LastYear() {
        List<Book> top100InPastYear = bookRepository.findTop100InPastYear();
        assertThat(top100InPastYear.size()).isEqualTo(1);
    }
}