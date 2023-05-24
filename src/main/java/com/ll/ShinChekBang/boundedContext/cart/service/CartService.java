package com.ll.ShinChekBang.boundedContext.cart.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.base.service.BaseService;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.repository.BookRepository;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService implements BaseService<Cart> {
    private final CartRepository cartRepository;

    @Transactional
    public RsData<Cart> addToCart(Cart cart, Book book) {
        if (cart.getBooks().contains(book)) {
            return RsData.of("F-10", "이미 장바구니에 상품이 담겨있습니다.");
        }
        if (cart.getMember().getBooks().contains(book)) {
            return RsData.of("F-10", "이미 소장하신 도서입니다.");
        }
        cart.getBooks().add(book);
        Cart cartAfterAddBooks = cartRepository.save(cart);
        return RsData.successOf(cartAfterAddBooks);
    }

    @Override
    public RsData<Cart> findOne(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()) {
            return RsData.of("F-11", "장바구니를 찾을 수 없습니다.");
        }
        return RsData.successOf(optionalCart.get());
    }

    @Transactional
    public RsData<Cart> removeFromCart(Cart cart, Book book) {
        cart.getBooks().remove(book);
        cartRepository.save(cart);
        return RsData.of("S-12", "장바구니에서 성공적으로 삭제했습니다.");
    }
}
