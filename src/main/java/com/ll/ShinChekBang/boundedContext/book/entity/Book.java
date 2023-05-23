package com.ll.ShinChekBang.boundedContext.book.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.base.file.entity.UploadFile;
import com.ll.ShinChekBang.base.ut.Utils;
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
    private float rate;
    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Category> categories = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @Builder.Default
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private UploadFile thumbnail;
    @OneToMany(cascade = CascadeType.ALL)
    private List<UploadFile> images;

    public void addReview(Review review) {
        reviews.add(review);
        review.setBook(this);
        double rateUnRounded = reviews.stream().mapToDouble(Review::getRate).sum() / reviews.size();
        rate = Utils.round((float) rateUnRounded, 2);
    }
}
