package com.ll.ShinChekBang.base.initData;

import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.book.service.BookService;
import com.ll.ShinChekBang.boundedContext.cart.service.CartService;
import com.ll.ShinChekBang.boundedContext.category.entity.Category;
import com.ll.ShinChekBang.boundedContext.category.entity.ParentCategory;
import com.ll.ShinChekBang.boundedContext.category.service.CategoryService;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.member.service.MemberService;
import com.ll.ShinChekBang.boundedContext.order.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Configuration
@Profile({"dev","test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            BookService bookService,
            CategoryService categoryService,
            OrderService orderService,
            CartService cartService
    ) {
        return new TransactionalCommandLineRunner(memberService, bookService, categoryService, orderService, cartService);
    }

    @Transactional
    protected void categorySet(CategoryService categoryService) {
        ParentCategory novel = categoryService.createParentCategory("소설").getData();
        ParentCategory economy = categoryService.createParentCategory("경영/경제").getData();
        ParentCategory social = categoryService.createParentCategory("인문/사회/역사").getData();
        ParentCategory selfDev = categoryService.createParentCategory("자기계발").getData();
        ParentCategory poem = categoryService.createParentCategory("에세이/시").getData();
        ParentCategory travel = categoryService.createParentCategory("여행").getData();

        categoryService.createCategory(novel, "한국소설");
        categoryService.createCategory(novel, "영미소설");
        categoryService.createCategory(novel, "일본소설");
        categoryService.createCategory(economy, "경영일반");
        categoryService.createCategory(economy, "경영일반");
        categoryService.createCategory(economy, "경제일반");
        categoryService.createCategory(economy, "마케팅/세일즈");
        categoryService.createCategory(travel, "국내여행");
        categoryService.createCategory(travel, "해외여행");
    }

    private class TransactionalCommandLineRunner implements CommandLineRunner {
        private final MemberService memberService;
        private final BookService bookService;
        private final CategoryService categoryService;
        private final OrderService orderService;
        private final CartService cartService;

        public TransactionalCommandLineRunner(MemberService memberService, BookService bookService, CategoryService categoryService, OrderService orderService, CartService cartService) {
            this.memberService = memberService;
            this.bookService = bookService;
            this.categoryService = categoryService;
            this.orderService = orderService;
            this.cartService = cartService;
        }

        @Transactional
        @Override
        public void run(String... args) throws IOException {
            // Your logic here
            categorySet(categoryService);
            Category koreanNovel = categoryService.findByName("한국소설").getData();

            Member member = memberService.join("user1", "1234", "1234", "asd@asd.com").getData();
            Member member2 = memberService.join("user2", "1234", "1234", "asf@asd.com").getData();
            Member member3 = memberService.join("user3", "1234", "1234", "bsf@asd.com").getData();
            Member admin = memberService.join("admin", "1234", "1234", "admin@lwu.me").getData();
            Book book1 = bookService.addNewBook("책1", "글쓴이", "소개1", 100, null, null).getData();
            Book book2 = bookService.addNewBook("책2", "글쓴이", "소개2", 1000, null, null).getData();
            Book book3 = bookService.addNewBook("책3", "글쓴이", "소개3", 10000, null, null).getData();
            categoryService.categorize(book1, koreanNovel);
            categoryService.categorize(book2, koreanNovel);
            categoryService.categorize(book3, koreanNovel);

            for (int i = 4; i <= 222; i++) {
                Book book = bookService.addNewBook("책%d".formatted(i), "글쓴이", "소개%d".formatted(i), i * 100, null, null).getData();
                categoryService.categorize(book, koreanNovel);
            }

            if (member2 != null) {
                cartService.addToCart(member2.getCart(), book1);
                orderService.order(member2, member2.getCart().getBooks(), member2.getCart().getTotalPrice());
            }

            if (admin != null) {
                cartService.addToCart(admin.getCart(), book1);
                orderService.order(admin, admin.getCart().getBooks(), admin.getCart().getTotalPrice());
            }

            if (member3 != null) {
                cartService.addToCart(member3.getCart(), book1);
                orderService.order(member3, member3.getCart().getBooks(), member3.getCart().getTotalPrice());
            }
        }
    }
}
