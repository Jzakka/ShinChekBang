package com.ll.ShinChekBang.boundedContext.review.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Review extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "memberId")
    @ToString.Exclude
    private Member member;

    @ManyToOne
    @JoinColumn(name = "bookId")
    @Setter
    private Book book;

    @Setter
    private float rate;
    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;
}
