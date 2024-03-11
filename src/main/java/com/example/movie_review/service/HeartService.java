package com.example.movie_review.service;

import com.example.movie_review.domain.review.Heart;
import com.example.movie_review.domain.review.Review;
import com.example.movie_review.domain.User;
import com.example.movie_review.repository.HeartRepository;
import com.example.movie_review.repository.ReviewRepository;
import com.example.movie_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 좋아요 추가
     */
    @Transactional
    public void addHeart(String loginId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        User loginUser = userRepository.findByLoginId(loginId).get();
        User reviewUser = review.getUser();

        if(reviewUser.equals(loginUser)) {
            reviewUser.updateHeartCnt(reviewUser.getReceivedHeartCnt() + 1);
        }
        review.updateHeartCnt(review.getHeartCnt() + 1);
//        loginUser.updatePressHeartCnt(loginUser.getPressHeartCnt() + 1);


        heartRepository.save(Heart.builder()
                .user(loginUser)
                .review(review)
                .build());
        System.out.println("Added heart for reviewId: " + reviewId + ", new heart count: " + review.getHeartCnt());
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void deleteHeart(String loginId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        User loginUser = userRepository.findByLoginId(loginId).get();
        User reviewUser = review.getUser();

        if(reviewUser.equals(loginUser)) {
            reviewUser.updateHeartCnt(reviewUser.getReceivedHeartCnt() - 1);
        }
        review.updateHeartCnt(review.getHeartCnt() - 1);
//        loginUser.updatePressHeartCnt(loginUser.getPressHeartCnt() - 1);

        heartRepository.deleteByUserLoginIdAndReviewId(loginId, reviewId);
        System.out.println("Deleted heart for reviewId: " + reviewId + ", new heart count: " + review.getHeartCnt());

    }

    public Boolean checkHeart(String loginId, Long reviewId) {
        return heartRepository.existsByUserLoginIdAndReviewId(loginId, reviewId);
    }


}
