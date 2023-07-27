package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    List<Book> findByTitle(String title);

    Page<Book> findByCategories(Category category, Pageable pageable);

    Page<Book> findByCategoriesAndPrice(Category category, Pageable pageable, int price);

    Page<Book> findByCategoriesIsIn(List<Category> categories, Pageable pageable);

    Page<Book> findByCategoriesIsInAndPrice(List<Category> categories, Pageable pageable, int price);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
