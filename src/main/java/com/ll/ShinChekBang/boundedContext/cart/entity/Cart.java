package com.ll.ShinChekBang.boundedContext.cart.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
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
public class Cart extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "memberId")
    @Setter
    @ToString.Exclude
    Member member;
    private int totalPrice;

    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    List<Book> books = new ArrayList<>();

    public int getTotalPrice() {
        return books.stream().mapToInt(Book::getPrice).sum();
    }
}
