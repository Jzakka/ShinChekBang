package com.ll.ShinChekBang.boundedContext.order.controller;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.base.ut.Utils;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import com.ll.ShinChekBang.boundedContext.order.entity.Order;
import com.ll.ShinChekBang.boundedContext.order.service.OrderService;
import com.ll.ShinChekBang.boundedContext.order.temporary.Bill;
import com.ll.ShinChekBang.boundedContext.order.temporary.Receipt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final BookService bookService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String goToOrderPage(@AuthenticationPrincipal User user,
                                @RequestParam List<Long> bookIds,
                                Model model) {
        Member member = memberService.getMember(user);
        List<Book> books = bookIds.stream().map(id -> Utils.getData(id, bookService)).toList();
        Bill bill = orderService.makeBill(books);
        model.addAttribute("books", books);
        model.addAttribute("bill", bill);

        return "order/order";
    }

    @GetMapping("/success")
    public String order(@AuthenticationPrincipal User user,
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

        Receipt receipt = new Receipt(paymentResult.getData());
        model.addAttribute(receipt);

        return "order/success";
    }
}
