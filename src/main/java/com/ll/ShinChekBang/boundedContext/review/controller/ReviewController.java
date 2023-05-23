package com.ll.ShinChekBang.boundedContext.review.controller;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import com.ll.ShinChekBang.boundedContext.review.entity.Review;
import com.ll.ShinChekBang.boundedContext.review.form.ReviewForm;
import com.ll.ShinChekBang.boundedContext.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final BookService bookService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{bookId}")
    public String writeReview(@PathVariable Long bookId,
                              @Valid ReviewForm reviewForm,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal User user,
                              RedirectAttributes redirectAttributes) {
        String redirectUrl = "redirect:/books/" + bookId;

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            // 에러 메시지 리스트를 RedirectAttributes에 추가
            redirectAttributes.addFlashAttribute("errors", errors);
            return redirectUrl;
        }

        Member reviewer = memberService.findByUsername(user.getUsername()).getData();
        Book book = bookService.findOne(bookId).getData();
        RsData<Review> reviewResult = reviewService.review(reviewer, book, reviewForm.getRate(), reviewForm.getContent());

        if (reviewResult.isFail()) {
            bindingResult.reject(reviewResult.getResultCode(), reviewResult.getMsg());
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            // 에러 메시지 리스트를 RedirectAttributes에 추가
            redirectAttributes.addFlashAttribute("errors", errors);
            return redirectUrl;
        }

        return redirectUrl;
    }
}
