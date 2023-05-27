package com.ll.ShinChekBang.boundedContext.book.controller;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.review.form.ReviewForm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
        @NotNull
        @NotEmpty
        @NotBlank
        String description;
        int price;
    }

    @GetMapping
    public String books(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Book> books = bookService.showBooks(page);
        model.addAttribute("books", books);
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
                bookForm.description,
                bookForm.price,
                thumbnail,
                images);
        if (addResult.isFail()) {
            bindingResult.reject(addResult.getResultCode(), addResult.getMsg());
            return "/books/new";
        }

        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String bookInfo(@PathVariable long id, Model model, ReviewForm reviewForm) {
        RsData<Book> bookResult = bookService.findOne(id);
        if (bookResult.isSuccess()) {
            model.addAttribute("book", bookResult.getData());
            return "/books/info";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, bookResult.getMsg());
    }
}
