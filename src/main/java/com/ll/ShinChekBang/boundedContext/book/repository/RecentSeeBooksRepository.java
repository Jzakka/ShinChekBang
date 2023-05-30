package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional
public class RecentSeeBooksRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private ListOperations<String, Object> listOperations;

    private static final int MAX_PRODUCTS = 20;

    @PostConstruct
    public void init() {
        listOperations = redisTemplate.opsForList();
    }

    public void saveBook(Long userId, Book book) {
        String key = userId.toString() + ":recentSeeBooks";
        listOperations.rightPush(key, book);
        listOperations.trim(key, 0, MAX_PRODUCTS - 1);
    }

    public List<Book> getBooks(Long userId) {
        String key = userId.toString() + ":recentSeeBooks";
        return Objects.requireNonNull(listOperations.range(key, 0, -1)).stream()
                .map(obj->(Book)obj).collect(Collectors.toList());
    }

    public void clear(Long userId) {
        String key = userId.toString() + ":recentSeeBooks";
        redisTemplate.delete(key);
    }
}
