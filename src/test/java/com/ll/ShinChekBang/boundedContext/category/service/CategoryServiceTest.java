package com.ll.ShinChekBang.boundedContext.category.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryServiceTest {
    @Autowired
    BookService bookService;
    @Autowired
    CategoryService categoryService;

    @Test
    void 카테고리분류() {
        Book book1 = bookService.findByTitle("책1").getData().get(0);
        Category category1 = categoryService.findByName("국내여행").getData();
        RsData<Category> categorizedResult = categoryService.categorize(book1, category1);
        assertThat(categorizedResult.isSuccess()).isTrue();
        assertThat(book1.getCategories()).contains(category1);
        assertThat(category1.getBooks()).contains(book1);
    }
}