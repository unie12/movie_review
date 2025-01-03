package com.example.movie_review.heart;

import com.example.movie_review.review.Review;
import com.example.movie_review.review.event.ReviewEvent;
import com.example.movie_review.review.service.ReviewMovieDTOService;
import com.example.movie_review.review.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review/heart")
public class HeartController {
    private final HeartService heartService;
    private final ReviewService reviewService;


    /**
     * 해당 리뷰에 좋아요 추가 및 삭제
     */
    @PostMapping("/{reviewId}")
    public ResponseEntity<?> toggleHeart(@RequestBody HeartRequest request, Authentication principal) {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HeartResponse("User not authenticated", false, 0));
        }
        try {
            String email = principal.getName();
            boolean isHeart = heartService.toggleHeart(email, request.getReviewId(), request.isHeart());

            Review updatedReview = reviewService.getReviewById(request.getReviewId());
            int updateHeartCount = updatedReview.getHeartCount();

            return ResponseEntity.ok(new HeartResponse("Heart toggled successfully", isHeart, updateHeartCount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HeartResponse(e.getMessage(), false, reviewService.getReviewById(request.getReviewId()).getHeartCount()));
        }
    }
}

@Data
class HeartRequest {
    private Long reviewId;
    private boolean heart;
}

@Data
@AllArgsConstructor
class HeartResponse {
    private String message;
    private boolean isHeart;
    private Integer updateHeartCnt;
}