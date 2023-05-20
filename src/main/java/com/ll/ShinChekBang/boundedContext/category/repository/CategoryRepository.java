package com.ll.ShinChekBang.boundedContext.category.repository;

import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
