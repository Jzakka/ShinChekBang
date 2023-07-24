package com.ll.ShinChekBang.boundedContext.book.repository;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);

    Page<Book> findByCategories(Category category, Pageable pageable);

    Page<Book> findByCategoriesIsIn(List<Category> categories, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
