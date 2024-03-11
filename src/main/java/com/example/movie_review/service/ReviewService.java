package com.example.movie_review.service;

import com.example.movie_review.domain.DTO.HeartRequestDto;
import com.example.movie_review.domain.DTO.ReviewCreateRequest;
import com.example.movie_review.domain.DTO.ReviewDto;
import com.example.movie_review.domain.User;
import com.example.movie_review.domain.review.Comment;
import com.example.movie_review.domain.review.Heart;
import com.example.movie_review.domain.review.Review;
import com.example.movie_review.repository.CommentRepository;
import com.example.movie_review.repository.HeartRepository;
import com.example.movie_review.repository.ReviewRepository;
import com.example.movie_review.repository.UserRepository;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HeartRepository heartRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> findReviews() {
        return reviewRepository.findAll();
    }

    public Page<Review> findReviews(Specification<Review> spec, Pageable pageable) {
        return reviewRepository.findAll(spec, pageable);
    }

    public Page<Review> findReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Review findOne(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public List<Review> findMyReview(String category, String loginId) {
        // 작성한 리뷰
        if(category.equals("review")) {
            return  reviewRepository.findAllByUserLoginId(loginId);
        }
        // 좋아요한 리뷰
        else if(category.equals("heart")) {
            List<Heart> hearts = heartRepository.findAllByUserLoginId(loginId);
            List<Review> reviews = new ArrayList<>();
            for(Heart heart : hearts) {
                reviews.add(heart.getReview());
            }
            return reviews;
        }
        // 댓글 단 리뷰
        else if(category.equals("comment")) {
            List<Comment> comments = commentRepository.findByUserLoginId(loginId);
            List<Review> reviews = new ArrayList<>();
            HashSet<Long> commentIds = new HashSet<>();

            for(Comment comment : comments) {
                if(!commentIds.contains(comment.getReview().getId())) {
                    reviews.add(comment.getReview());
                    commentIds.add(comment.getReview().getId());
                }
            }
            return reviews;
        }
        return null;
    }

    @Transactional
    public Long editReview(ReviewDto req, Long reviewId) {
        Optional<Review> optReview = reviewRepository.findById(reviewId);

        if(optReview.isEmpty()) {
            return null;
        }

        Review review = optReview.get();
        review.editReview(req);

        return review.getId();
    }

    public ReviewDto getReview(Long reviewId) {
        Optional<Review> optReview = reviewRepository.findById(reviewId);

        if(optReview.isEmpty()) {
            return null;
        }

        return ReviewDto.of(optReview.get());
    }

    @Transactional
    public Long deleteReview(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if(optionalReview.isEmpty()) {
            return null;
        }

        Review review = optionalReview.get();
        User reviewUser = review.getUser();
        reviewUser.updateHeartCnt(reviewUser.getReceivedHeartCnt() - review.getHeartCnt());
        reviewRepository.deleteById(reviewId);
        return reviewId;
    }

    public Page<Review> reviewSearchList(String keyword, Pageable pageable) {
        return reviewRepository.findByTitleContaining(keyword, pageable);
    }
}
