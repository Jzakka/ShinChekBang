package com.ll.ShinChekBang.boundedContext.member.controller;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        return "/member/login";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupForm {
        @Email
        String email;
        @NotNull
        String username;
        @Size(min = 6, max = 20)
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}",
                message = "비밀번호는 숫자, 소문자, 대문자, 특수 문자가 최소 하나씩 포함되어야 합니다.")
        String password;
        @NotNull
        String passwordConfirm;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String join(SignupForm signupForm) {
        return "/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid MemberController.SignupForm signupForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/member/join";
        }

        RsData<Member> joinResult = memberService.join(signupForm.username, signupForm.password, signupForm.passwordConfirm, signupForm.email);
        if (joinResult.isFail()) {
            bindingResult.reject(joinResult.getResultCode(), joinResult.getMsg());
            return "/member/join";
        }

        return "redirect:/";
    }
}
