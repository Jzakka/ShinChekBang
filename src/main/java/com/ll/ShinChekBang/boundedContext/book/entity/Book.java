package com.ll.ShinChekBang.boundedContext.book.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Book extends BaseEntity {
    private String title;
    private String author;
    private int price;
    @Setter
    private int stock;
    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Category> categories = new ArrayList<>();
}
