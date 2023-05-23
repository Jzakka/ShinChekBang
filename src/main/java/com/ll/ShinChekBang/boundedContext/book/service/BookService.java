package com.ll.ShinChekBang.boundedContext.book.service;

import com.ll.ShinChekBang.base.file.service.FileService;
import com.ll.ShinChekBang.base.file.entity.UploadFile;
import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final FileService fileService;

    @Transactional
    public RsData<Book> addNewBook(String title, String author, Integer price, MultipartFile thumbnail, List<MultipartFile> images) throws IOException {
        UploadFile thumbnailFile = fileService.storeFile(thumbnail);
        List<UploadFile> imageFiles = fileService.storeFile(images);

        Book book = Book.builder()
                .title(title)
                .author(author)
                .price(price)
                .thumbnail(thumbnailFile)
                .images(imageFiles)
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

    @Transactional
    public RsData<Book> adjustStock(Book book, int stock) {
        book.setStock(stock);
        Book adjustedBook = bookRepository.save(book);
        return RsData.successOf(adjustedBook);
    }

    public RsData<List<Book>> findByTitle(String title) {
        return RsData.successOf(bookRepository.findByTitle(title));
    }

    public List<Book> showBooks() {
        return bookRepository.findAll();
    }

    public Page<Book> showBooks(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        return bookRepository.findAll(pageable);
    }
}
