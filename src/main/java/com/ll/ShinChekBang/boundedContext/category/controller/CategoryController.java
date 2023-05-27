package com.ll.ShinChekBang.boundedContext.category.controller;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import com.ll.ShinChekBang.boundedContext.category.entity.ParentCategory;
import com.ll.ShinChekBang.boundedContext.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    private final BookService bookService;

    @GetMapping
    public String categories(Model model) {
        List<ParentCategory> parentCategories = categoryService.findParentCategories();
        model.addAttribute("parentCategories", parentCategories);
        return "category/list";
    }

    @GetMapping("/{categoryId}")
    public String showBooksInCateogry(@PathVariable Long categoryId,
                                      @RequestParam(defaultValue = "0") int page,
                                      Model model) {
        Category category = categoryService.findOne(categoryId);
        Page<Book> books = bookService.findByCategory(category, page);
        model.addAttribute("category", category);
        model.addAttribute("books", books);
        return "/books/books";
    }
}
