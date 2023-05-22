package com.ll.ShinChekBang.base.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) {
        // 로그인 실패 원인에 대한 정보를 확인하고 처리합니다.
        if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            // 비밀번호가 잘못된 경우
            System.out.println("Password unmatch");
        } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
            // 사용자 계정이 비활성화된 경우
            System.out.println("Account inactived");
        } else if (exception.getClass().isAssignableFrom(AccountExpiredException.class)) {
            // 사용자 계정이 만료된 경우
            System.out.println("Account expired");
        } else if (exception.getClass().isAssignableFrom(LockedException.class)) {
            // 사용자 계정이 잠긴 경우
            System.out.println("Account blocked");
        }
    }
}
