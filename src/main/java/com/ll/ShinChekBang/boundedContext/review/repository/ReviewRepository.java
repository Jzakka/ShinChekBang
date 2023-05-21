package com.ll.ShinChekBang.boundedContext.review.repository;

import com.ll.ShinChekBang.boundedContext.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
