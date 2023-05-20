package com.ll.ShinChekBang.boundedContext.cart.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
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
public class CartBook extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "cartId")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;

    @Setter
    private int quantity;
    @Setter
    private int priceSum;

    public static CartBook of(Cart cart, Book book) {
        return CartBook.builder()
                .cart(cart)
                .book(book)
                .build();
    }

    public static CartBook of(Cart cart, Book book, int quantity) {
        return CartBook.builder()
                .cart(cart)
                .book(book)
                .quantity(quantity)
                .priceSum(book.getPrice()*quantity)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartBook cartBook = (CartBook) o;
        return Objects.equals(getCart(), cartBook.getCart()) && Objects.equals(getBook(), cartBook.getBook());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCart(), getBook());
    }
}
