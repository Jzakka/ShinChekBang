package com.ll.ShinChekBang.boundedContext.order.controller;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.base.ut.Utils;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final BookService bookService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String goToOrderPage(@RequestParam List<Long> bookIds,
                                @RequestParam int paymentAccount,
                                Model model) {
        List<Book> books = bookIds.stream().map(id -> Utils.getData(id, bookService)).toList();
        model.addAttribute("books", books);
        model.addAttribute("paymentAccount", paymentAccount);

        return "order/order";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String order(@AuthenticationPrincipal User user,
                        @RequestParam List<Long> bookIds,
                        @RequestParam int paymentAccount) {
        Member member = memberService.getMember(user);
        List<Book> books = bookIds.stream().map(id -> Utils.getData(id, bookService)).toList();
        RsData<Order> order = orderService.order(member, books, paymentAccount);


        return "order/order";
    }
}
