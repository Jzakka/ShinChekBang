package com.ll.ShinChekBang.boundedContext.category.repository;

import com.ll.ShinChekBang.boundedContext.category.entity.ParentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentCategoryRepository extends JpaRepository<ParentCategory, Long> {
    Optional<ParentCategory> findByName(String name);
}
