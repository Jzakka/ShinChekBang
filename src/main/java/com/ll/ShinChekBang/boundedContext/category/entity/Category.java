package com.ll.ShinChekBang.boundedContext.category.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class Category extends BaseEntity {
    @Column(unique = true)
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    @Builder.Default
    private List<Book> books = new ArrayList<>();

    @ManyToOne
    @Setter
    private ParentCategory parentCategory;

    public void include(Book book) {
        books.add(book);
        book.getCategories().add(this);
    }
}
