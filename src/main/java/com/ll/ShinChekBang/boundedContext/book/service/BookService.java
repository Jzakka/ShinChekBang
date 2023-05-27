package com.ll.ShinChekBang.boundedContext.book.service;

import com.ll.ShinChekBang.base.file.service.FileService;
import com.ll.ShinChekBang.base.file.entity.UploadFile;
import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.base.service.BaseService;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import com.ll.ShinChekBang.boundedContext.category.entity.ParentCategory;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService implements BaseService<Book> {
    private final BookRepository bookRepository;
    private final FileService fileService;

    @Transactional
    public RsData<Book> addNewBook(String title, String author, String description, Integer price, MultipartFile thumbnail, List<MultipartFile> images) throws IOException {
        UploadFile thumbnailFile = fileService.storeFile(thumbnail);
        List<UploadFile> imageFiles = fileService.storeFile(images);

        Book book = Book.builder()
                .title(title)
                .author(author)
                .description(description)
                .price(price)
                .thumbnail(thumbnailFile)
                .images(imageFiles)
                .build();
        Book newBook = bookRepository.save(book);

        return RsData.of("S-1", "새로운 책을 등록했습니다.", newBook);
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

    @Override
    public RsData<Book> findOne(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return RsData.of("F-8", "상품정보를 찾을 수 없습니다.");
        }
        return RsData.successOf(optionalBook.get());
    }

    @Transactional
    public RsData<Book> addNewBook(String title, String author, int price, MultipartFile thumbnail, List<MultipartFile> images) throws IOException {
        return addNewBook(title, author, "책 소개가 없습니다.", price, thumbnail, images);
    }

    public Page<Book> findByCategory(Category category, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 60, Sort.by(sorts));
        return bookRepository.findByCategories(category, pageable);
    }

    public Page<Book> findByCategory(ParentCategory parentCategory, int page) {
        List<Category> categories = parentCategory.getChildCategories();
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 60, Sort.by(sorts));
        return bookRepository.findByCategoriesIsIn(categories, pageable);
    }
}
