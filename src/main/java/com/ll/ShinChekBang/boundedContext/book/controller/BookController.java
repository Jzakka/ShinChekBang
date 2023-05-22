package com.ll.ShinChekBang.boundedContext.book.controller;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @Data
    public static class BookForm {
        @NotNull
        String title;
        @NotNull
        String author;
        int price;
        int stock;
    }

    @GetMapping
    public String books(Model model) {
        //TODO 페이징+쿼리스트링 필요
        List<Book> listRsData = bookService.showBooks();
        model.addAttribute("books", listRsData);
        return "/books/recent";
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('admin')")
    @GetMapping("/new")
    public String newBook(BookForm bookForm) {
        return "/books/new";
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('admin')")
    @PostMapping("/new")
    public String newBook(@Valid BookForm bookForm,
                          BindingResult bindingResult,
                          @RequestParam MultipartFile thumbnail,
                          @RequestParam List<MultipartFile> images) throws IOException {
        if (bindingResult.hasErrors()) {
            return "/books/new";
        }

        RsData<Book> addResult = bookService.addNewBook(
                bookForm.title,
                bookForm.author,
                bookForm.price,
                thumbnail,
                images);
        if (addResult.isFail()) {
            bindingResult.reject(addResult.getResultCode(), addResult.getMsg());
            return "/books/new";
        }

        RsData<Book> adjustResult = bookService.adjustStock(addResult.getData(), bookForm.stock);
        if (adjustResult.isFail()) {
            bindingResult.reject(adjustResult.getResultCode(), adjustResult.getMsg());
            return "/books/new";
        }

        return "redirect:/books";
    }
}
