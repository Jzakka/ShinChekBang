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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    private final BookService bookService;

    @ModelAttribute("parentCategories")
    public List<ParentCategory> parentCategories() {
        return categoryService.parentCategories();
    }

    @GetMapping
    public String categories() {
        return "category/list";
    }

    @GetMapping("/{categoryId}")
    public String showBooksInCateogory(@PathVariable Long categoryId,
                                      @RequestParam(defaultValue = "0") int page,
                                      Model model) {
        Category category = categoryService.findOne(categoryId);
        Page<Book> books = bookService.findByCategory(category, page);
        model.addAttribute("category", category);
        model.addAttribute("books", books);
        return "/books/books";
    }

    @GetMapping("/of/{parentCategoryId}")
    public String showBooksInParentCategory(@PathVariable Long parentCategoryId,
                                            @RequestParam(defaultValue = "0") int page,
                                            Model model) {
        ParentCategory category = categoryService.findParentOne(parentCategoryId);
        Page<Book> books = bookService.findByCategory(category, page);
        model.addAttribute("parentCategory", category);
        model.addAttribute("books", books);
        return "/books/allBooks";
    }
}
