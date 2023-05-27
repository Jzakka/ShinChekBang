package com.ll.ShinChekBang.boundedContext.category.controller;

import com.ll.ShinChekBang.boundedContext.category.entity.ParentCategory;
import com.ll.ShinChekBang.boundedContext.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String categories(Model model) {
        List<ParentCategory> parentCategories = categoryService.findParentCategories();
        model.addAttribute("parentCategories", parentCategories);
        return "category/list";
    }
}
