package com.ll.ShinChekBang.boundedContext.order.controller;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.base.ut.Utils;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.cart.service.CartService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.service.OrderService;
import com.ll.ShinChekBang.boundedContext.order.temporary.Bill;
import com.ll.ShinChekBang.boundedContext.order.temporary.Receipt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final BookService bookService;
    private final CartService cartService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String goToOrderPage(@RequestParam List<Long> bookIds, Model model) {
        List<Book> books = bookIds.stream().map(id -> Utils.getData(id, bookService)).toList();
        Bill bill = orderService.makeBill(books);
        model.addAttribute("books", books);
        model.addAttribute("bill", bill);

        return "order/order";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/success")
    public String orderSuccess(@AuthenticationPrincipal User user,
                               @RequestParam String orderId,
                               @RequestParam int amount,
                               @RequestParam String paymentKey,
                               Model model) {
        Member member = memberService.getMember(user);

        // 토스페이먼츠 서버와 통신
        RsData<Order> paymentResult = orderService.pay(member, orderId, amount, paymentKey);

        if (paymentResult.isFail()) {
            return "redirect:/order/fail";
        }

        // 장바구니 비우기
        cartService.takeOff(member, paymentResult.getData().getBooks());

        Receipt receipt = new Receipt(paymentResult.getData());
        model.addAttribute(receipt);

        return "order/success";
    }

    @GetMapping("/fail")
    public String orderFail() {
        return "error/4xx";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history")
    public String orderHistory(@AuthenticationPrincipal User user,
                               @RequestParam(required = false, defaultValue = "0") int page,
                               Model model) {
        Member member = memberService.getMember(user);

        Page<Receipt> orders = orderService.getOrders(member, page);

        model.addAttribute("orders",orders);

        return "member/order-history";
    }
}
