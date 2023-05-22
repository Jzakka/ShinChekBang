package com.ll.ShinChekBang.boundedContext.member.controller;

import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    public String info() {
        System.out.println("MemberController.info");
        return "/member/info";
    }

    @GetMapping("/login")
    public String login() {
        return "/member/login";
    }
}
