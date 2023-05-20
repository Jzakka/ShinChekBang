package com.ll.ShinChekBang.boundedContext.book.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Transactional
    public RsData<Book> store(Book book, int quantity) {
        book.setStock(book.getStock() + quantity);
        Book storedBook = bookRepository.save(book);
        return RsData.of("S-5", "%d번 상품의 재고가 %d개가 되었습니다.".formatted(book.getId(), book.getStock()), storedBook);
    }

    public RsData<List<Book>> findByTitle(String title) {
        return RsData.successOf(bookRepository.findByTitle(title));
    }
}
