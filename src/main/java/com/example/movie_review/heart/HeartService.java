package com.example.movie_review.heart;

import com.example.movie_review.review.Review;
import com.example.movie_review.review.ReviewService;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final UserService userService;
    private final ReviewService reviewService;

    public boolean toggleHeart(String email, Long reviewId, boolean isHeart) {
        User user = userService.getUserByEmail(email);
        Review review = reviewService.getReviewById(reviewId);

        Optional<Heart> existingHeart = heartRepository.findByUserAndReview(user, review);

        // 좋아요 안눌린 상태
        if(isHeart && existingHeart.isEmpty()) {
            Heart heart = new Heart();
            heart.setUser(user);
            heart.setReview(review);
//            review.incrementHeartCnt();
            heartRepository.save(heart);
            reviewService.saveReview(review);
            return true;

        }
        // 좋아요 눌려 있던 상태
        else if (!isHeart && existingHeart.isPresent()){
//            review.decrementHeartCnt();
            heartRepository.delete(existingHeart.get());
            reviewService.saveReview(review);
            return false;
        }
        return existingHeart.isPresent();
    }

    public List<Long> getUserHearts(String email) {
        User user = userService.getUserByEmail(email);

        return heartRepository.findByUser(user).stream()
                .map(heart -> heart.getReview().getId())
                .collect(Collectors.toList());

    }
}
