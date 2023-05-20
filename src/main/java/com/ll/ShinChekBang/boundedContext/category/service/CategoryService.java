package com.ll.ShinChekBang.boundedContext.category.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import com.ll.ShinChekBang.boundedContext.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Transactional
    public RsData<Category> createCategory(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        if (optionalCategory.isPresent()) {
            return RsData.of("F-1", "이미 존재하는 카테고리 명입니다.");
        }

        Category category = Category.builder()
                .name(name)
                .build();
        Category newCategory = categoryRepository.save(category);

        return RsData.of("S-1", "신규 카테고리[%s]가 생성되었습니다.".formatted(category.getName()), newCategory);
    }

    public RsData<Category> findByName(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        if (optionalCategory.isEmpty()) {
            return RsData.of("F-6", "%s와 일치하는 이름의 카테고리가 없습니다.".formatted(name));
        }
        return RsData.successOf(optionalCategory.get());
    }

    public RsData<Category> categorize(Book book, Category category) {
        if (category.getBooks().contains(book)) {
            return RsData.of("F-7", "이미 존재하는 도서입니다.");
        }

        category.include(book);
        book = bookRepository.save(book);
        category = categoryRepository.save(category);
        return RsData.of("S-7", "카테고리[%s]에 %s가 추가되었습니다.".formatted(category.getName(), book.getTitle()));
    }
}
