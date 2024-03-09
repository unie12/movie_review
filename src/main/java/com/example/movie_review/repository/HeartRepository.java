package com.example.movie_review.repository;

import com.example.movie_review.domain.review.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    void deleteByUserLoginIdAndReviewId(String loginId, Long reviewId);
    Boolean existsByUserLoginIdAndReviewId(String loginId, Long reviewId);
    List<Heart> findAllByUserLoginId(String loginId);
}
