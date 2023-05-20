package com.ll.ShinChekBang.base.initData;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            BookService bookService
    ) {
        return args -> {
            Member member = memberService.join("user1", "1234", "1234", "asd@asd.com").getData();
            Book book1 = bookService.addNewBook("책1", "글쓴이", 100).getData();
            Book book2 = bookService.addNewBook("책2", "글쓴이", 1000).getData();
            Book book3 = bookService.addNewBook("책3", "글쓴이", 10000).getData();
        };
    }
}
