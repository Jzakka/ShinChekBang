package com.ll.ShinChekBang.boundedContext.cart.controller;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.base.service.BaseService;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.cart.entity.Cart;
import com.ll.ShinChekBang.boundedContext.cart.service.CartService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final BookService bookService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String goToMyCart(@AuthenticationPrincipal User user) {
        Member member = getMember(user);
        return "redirect:/cart/" + member.getCart().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId,
                            @AuthenticationPrincipal User user) {
        Book book = getData(bookId, bookService);
        Member member = getMember(user);

        RsData<Cart> addedResult = cartService.addToCart(member.getCart(), book);

        if (addedResult.isFail()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, addedResult.getMsg());
        }

        return "redirect:/cart/" + member.getCart().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{cartId}")
    public String showCart(@PathVariable Long cartId,
                           @AuthenticationPrincipal User user,
                           Model model) {
        Cart cart = getData(cartId, cartService);
        Member member = getMember(user);

        if (member.getCart().equals(cart)) {
            model.addAttribute("cart", cart);
            return "cart/cart";
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{cartId}/delete/{bookId}")
    public String deleteBookFromCart(@PathVariable Long cartId,
                                     @PathVariable Long bookId,
                                     @AuthenticationPrincipal User user) {
        Cart cart = getData(cartId, cartService);
        Member member = getMember(user);

        if (member.getCart().equals(cart)) {
            Book book = getData(bookId, bookService);
            cartService.removeFromCart(cart, book);
            return "redirect:/cart/" + cartId;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
    }

    private <T> T getData(Long dataId, BaseService<T> service) {
        RsData<T> findResult = service.findOne(dataId);
        if (findResult.isFail()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, findResult.getMsg());
        }
        return findResult.getData();
    }

    private Member getMember(User user) {
        RsData<Member> memberResult = memberService.findByUsername(user.getUsername());
        if (memberResult.isFail()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, memberResult.getMsg());
        }

        return memberResult.getData();
    }
}
