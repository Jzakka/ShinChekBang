package com.ll.ShinChekBang.boundedContext.category.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    public void include(Book book) {
        books.add(book);
        book.getCategories().add(this);
    }
}
