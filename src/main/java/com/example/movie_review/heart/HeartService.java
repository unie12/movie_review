package com.example.movie_review.heart;

import com.example.movie_review.review.Review;
import com.example.movie_review.review.event.ReviewEvent;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;

    private final UserService userService;
    private final ReviewService reviewService;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean toggleHeart(String email, Long reviewId, boolean isHeart) {
        User user = userService.getUserByEmail(email);
        Review review = reviewService.getReviewById(reviewId);
        Optional<Heart> existingHeart = heartRepository.findByUserAndReview(user, review);

        boolean result;
        // 좋아요 안눌린 상태
        if(isHeart && existingHeart.isEmpty()) {
            Heart heart = new Heart();
            heart.setUser(user);
            heart.setReview(review);
            heartRepository.save(heart);
            result = true;
        }
        // 좋아요 눌려 있던 상태
        else if (!isHeart && existingHeart.isPresent()){
            heartRepository.delete(existingHeart.get());
            result = false;
        } else {
            result = existingHeart.isPresent();
        }

        reviewService.saveReview(review);

        Review updatedReview = reviewService.getReviewById(reviewId);
        eventPublisher.publishEvent(new ReviewEvent(this, updatedReview, ReviewEvent.ReviewEventType.HEART));

        return result;
    }

    public List<Long> getUserHearts(String email) {
        User user = userService.getUserByEmail(email);

        return heartRepository.findByUser(user).stream()
                .map(heart -> heart.getReview().getId())
                .collect(Collectors.toList());

    }
}
