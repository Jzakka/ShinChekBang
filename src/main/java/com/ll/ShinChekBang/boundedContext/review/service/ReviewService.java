package com.ll.ShinChekBang.boundedContext.review.service;

import com.ll.ShinChekBang.base.result.RsData;
import com.ll.ShinChekBang.boundedContext.book.entity.Book;
import com.ll.ShinChekBang.boundedContext.member.entity.Member;
import com.ll.ShinChekBang.boundedContext.review.entity.Review;
import com.ll.ShinChekBang.boundedContext.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public RsData<Review> review(Member member, Book book, float rate, String content) {
        if (member.hasOrdered(book)) {
            Review review = Review.builder()
                    .member(member)
                    .book(book)
                    .rate(rate)
                    .content(content)
                    .build();
            Review uploadedReview = reviewRepository.save(review);
            return RsData.of("S-8", "리뷰를 성공적으로 작성했습니다.", uploadedReview);
        }
        return RsData.of("F-8", "구매하지 않은 서적에 대해선 리뷰작성이 불가능합니다.");
    }
}
