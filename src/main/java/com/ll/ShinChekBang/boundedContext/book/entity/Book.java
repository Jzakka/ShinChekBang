package com.ll.ShinChekBang.boundedContext.book.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.base.file.entity.UploadFile;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import com.ll.ShinChekBang.boundedContext.review.entity.Review;
import jakarta.persistence.*;
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private UploadFile thumbnail;
    @OneToMany(cascade = CascadeType.ALL)
    private List<UploadFile> images;
}
