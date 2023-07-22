package com.ll.ShinChekBang.boundedContext.order.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.order.temporary.Bill;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BillRepositoryTest {
    @Autowired
    BillRepository billRepository;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    @AfterEach
    void setup() {
        redisTemplate.execute((RedisCallback<? extends Object>) connection -> {
            connection.serverCommands().flushDb();
            return "OK";
        });
    }

    @Test
    @DisplayName("계산서 생성 후 조회")
    void makeBillAndShow() {
        List<Book> books = makeTempBooks();
        Member member = Member.builder().id(20L).build();

        Bill bill = new Bill( books, 400);
        billRepository.save(bill);

        Optional<Bill> optionalBill = billRepository.findById(bill.getId());
        assertThat(optionalBill).isPresent();
        Bill foundOne = optionalBill.get();
        assertThat(foundOne).isEqualTo(bill);
    }

    List<Book> makeTempBooks() {
        Book book1 = Book.builder().id(1L).title("book1").price(100).build();
        Book book2 = Book.builder().id(2L).title("book2").price(200).build();
        Book book3 = Book.builder().id(3L).title("book3").price(300).build();
        return List.of(book1, book2, book3);
    }
}