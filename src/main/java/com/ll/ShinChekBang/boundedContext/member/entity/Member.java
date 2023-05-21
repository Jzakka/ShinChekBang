package com.ll.ShinChekBang.boundedContext.member.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.entity.OrderBook;
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
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Setter
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Cart cart;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    public boolean hasOrdered(Book book) {
        for (Order order : orders) {
            for (OrderBook orderItem : order.getOrderItems()) {
                if (orderItem.getBook().equals(book)) {
                    return true;
                }
            }
        }
        return false;
    }
}
