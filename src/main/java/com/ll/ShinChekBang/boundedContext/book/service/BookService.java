package com.ll.ShinChekBang.boundedContext.book.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public RsData<Book> addNewBook(String title, String author, Integer price) {
        Book book = Book.builder()
                .title(title)
                .author(author)
                .price(price)
                .build();
        Book newBook = bookRepository.save(book);

        return RsData.of("S-1", "새로운 책을 등록했습니다.", newBook);
    }
}
