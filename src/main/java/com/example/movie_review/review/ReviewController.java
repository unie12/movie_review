package com.example.movie_review.review;

import com.example.movie_review.domain.review.Review;
import com.example.movie_review.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/review")
//public class ReviewController {
//    private final ReviewService reviewService;
//
//    @GetMapping("/{movieId}")
//    public ResponseEntity<?> loadReview(@PathVariable Long movieId, Model model, @AuthenticationPrincipal OAuth2User principal) {
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//
//        try {
//            String email = principal.getAttribute("email");
//            Optional<Review> review = reviewService.getReview(movieId, email);
//
//            if(review.isPresent()) {
//                return ResponseEntity.ok(new ReviewResponse(movieId, review.get().getContext()));
//            } else {
//                return ResponseEntity.ok(new ReviewResponse(movieId, null));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

//    @PostMapping("")
//    public ResponseEntity<?> saveReview(@RequestBody ReviewResponse response, @AuthenticationPrincipal OAuth2User principal) {
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//    }
//
//    @DeleteMapping("/{movieId}")
//    public ResponseEntity<?> deleteReview(@PathVariable Long movieId, @AuthenticationPrincipal OAuth2User principal) {
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//    }




//}

@Data
@AllArgsConstructor
class ReviewResponse {
    private Long movieId;
    private String review;
}
