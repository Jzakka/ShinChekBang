package com.ll.ShinChekBang.boundedContext.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.base.file.entity.UploadFile;
import com.ll.ShinChekBang.base.ut.Utils;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import com.ll.ShinChekBang.boundedContext.review.entity.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Book extends BaseEntity {
    private String title;
    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;
    private int price;

    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Category> categories = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private UploadFile thumbnail;


    @OneToMany(cascade = CascadeType.ALL)
    private List<UploadFile> images;

    @JsonIgnore
    public float getRate() {
        if (reviews.isEmpty()) {
            return 0;
        }
        double rateUnRounded = reviews.stream().mapToDouble(Review::getRate).sum() / reviews.size();
        return Utils.round((float) rateUnRounded, 2);
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setBook(this);
    }

    @JsonIgnore
    public float getOneRate() {
        return getRateOfStar(1);
    }
    @JsonIgnore
    public float getTwoRate() {
        return getRateOfStar(2);
    }
    @JsonIgnore
    public float getThreeRate() {
        return getRateOfStar(3);
    }
    @JsonIgnore
    public float getFourRate() {
        return getRateOfStar(4);
    }
    @JsonIgnore
    public float getFiveRate() {
        return getRateOfStar(5);
    }
    @JsonIgnore
    private float getRateOfStar(int rate) {
        if (reviews.isEmpty()) {
            return 0;
        }
        float rateUnRounded = (float) reviews.stream().filter(review -> review.getRate() == rate).count() / reviews.size();
        return Utils.round(rateUnRounded, 4) * 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Book book = (Book) o;
        return getId() != null && Objects.equals(getId(), book.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
