package com.ll.ShinChekBang.boundedContext.cart.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;

    @Transactional
    public RsData<Cart> addToCart(Cart cart, Book book) {
        if (cart.getBooks().contains(book)) {
            return RsData.of("F-10", "이미 장바구니에 상품이 담겨있습니다.");
        }
        cart.getBooks().add(book);
        Cart cartAfterAddBooks = cartRepository.save(cart);
        return RsData.successOf(cartAfterAddBooks);
    }
}
