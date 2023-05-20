package com.ll.ShinChekBang.boundedContext.cart.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.cartBook.entity.CartBook;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
    Member member;
    private int totalPrice;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<CartBook> cartBooks = new ArrayList<>();

    public void addBook(Book book, int quantity) {
        if (cartBooks.contains(CartBook.of(this, book))) {
            int idx = cartBooks.indexOf(CartBook.of(this, book));
            cartBooks.get(idx).setQuantity(cartBooks.get(idx).getQuantity() + quantity);
            cartBooks.get(idx).setPriceSum(cartBooks.get(idx).getQuantity() * book.getPrice());
        }
        cartBooks.add(CartBook.of(this, book, quantity));
        totalPrice += book.getPrice() * quantity;
    }
}
