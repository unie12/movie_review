package com.example.movie_review.repository;

import com.example.movie_review.domain.review.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByReviewId(Long reviewId);
    List<Comment> findByUserLoginId(String loginId);
}
