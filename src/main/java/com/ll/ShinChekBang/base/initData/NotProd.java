package com.ll.ShinChekBang.base.initData;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.cart.service.CartService;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import com.ll.ShinChekBang.boundedContext.category.service.CategoryService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import com.ll.ShinChekBang.boundedContext.order.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"test", "dev"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            BookService bookService,
            CategoryService categoryService,
            OrderService orderService,
            CartService cartService
    ) {
        return args -> {
            Member member = memberService.join("user1", "1234", "1234", "asd@asd.com").getData();
            Member member2 = memberService.join("user2", "1234", "1234", "asf@asd.com").getData();
            Member member3 = memberService.join("user3", "1234", "1234", "bsf@asd.com").getData();
            Member admin = memberService.join("admin", "1234", "1234", "admin@lwu.me").getData();
            Book book1 = bookService.addNewBook("책1", "글쓴이", "소개1", 100, null, null).getData();
            Book book2 = bookService.addNewBook("책2", "글쓴이", "소개2", 1000, null, null).getData();
            Book book3 = bookService.addNewBook("책3", "글쓴이", "소개3", 10000, null, null).getData();
            for (int i = 4; i <= 25; i++) {
                bookService.addNewBook("책%d".formatted(i), "글쓴이", "소개%d".formatted(i), i * 100, null, null);
            }
            Category category1 = categoryService.createCategory("카테고리1").getData();
            Category category2 = categoryService.createCategory("카테고리2").getData();

            cartService.addToCart(member2.getCart(), book1);
            orderService.order(member2);

            cartService.addToCart(admin.getCart(), book1);
            orderService.order(admin);

            cartService.addToCart(member3.getCart(), book1);
            orderService.order(member3);
        };
    }
}
