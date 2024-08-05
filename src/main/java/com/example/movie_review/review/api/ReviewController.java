package com.example.movie_review.review.api;

import com.example.movie_review.review.Review;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.review.DTO.ReviewMovieDTO;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.review.service.ReviewMovieDTOService;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.service.UserDTOService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMovieDTOService reviewMovieDTOService;
    private final UserDTOService userDTOService;

    /**
     * 해당 유저의 해당 영화에 대한 리뷰 보여주기
     */
    @GetMapping("/movie/{movieId}/review")
        public ResponseEntity<?> loadReview(@PathVariable Long movieId, Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            String email = principal.getAttribute("email");
            Optional<Review> review = reviewService.getReview(movieId, email);

            if(review.isPresent()) {
                return ResponseEntity.ok(new ReviewResponse(movieId, review.get().getContext()));
            } else {
                return ResponseEntity.ok(new ReviewResponse(movieId, null));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 리뷰를 새로 작성하거나 기존의 작성 리뷰 수정
     */
    @PostMapping("/movie/{movieId}/review")
    public ResponseEntity<?> saveReview(@RequestBody ReviewRequest request, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            String email = principal.getAttribute("email");
            Review savedReview = reviewService.saveOrUpdateReview(request.getMovieId(), email, request.getReview());

            return ResponseEntity.ok(new ReviewResponse(savedReview.getDbMovies().getId(), savedReview.getContext()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/movie/{movieId}/review")
    public ResponseEntity<?> deleteReview(@PathVariable Long movieId, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            String email = principal.getAttribute("email");
            reviewService.deleteReview(movieId, email);
            return ResponseEntity.ok(new DeleteResponse(movieId, "Review successfully deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 해당 영화에 대한 리뷰 전체 리스트
     * movieId == tId
     */
    @GetMapping("/movie/{movieTId}/reviews")
    public Page<ReviewDTO> getMovieReviews(
            @PathVariable Long movieTId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "heartCount,desc") String sort,
            Authentication principal) {
        return reviewMovieDTOService.getMovieReviews(movieTId, page, size, sort, principal.getName());
    }

    /**
     * 해당 리뷰에 좋아요 누른 사람들 리스트
     */
    @GetMapping("/review/{reviewId}/likes")
    public Page<UserCommonDTO> getReviewLikeUser(
            @PathVariable Long reviewId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userDTOService.getUsersByReviewLike(reviewId, pageable);
    }


    @GetMapping("/reviews/home-reviews")
    public List<ReviewMovieDTO> getHomeReviews() {
        return reviewMovieDTOService.getMixedHomeReviews(6);
    }

    /**
     * 전체 리뷰 보기 리스트
     */
    @GetMapping("/reviews")
    @ResponseBody
    public Page<ReviewMovieDTO> getReviews(
            @RequestParam(defaultValue = "popular") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if ("recently".equals(filter)) {
            return reviewMovieDTOService.getRecentReviews(PageRequest.of(page, size));
        } else {
            return reviewMovieDTOService.getPopularReviews(PageRequest.of(page, size));
        }
//        return reviewService.getReviews(filter, PageRequest.of(page, size));
    }

    @PostMapping("/reviews/refresh-cache")
    public ResponseEntity<?> refreshCache() {
        reviewMovieDTOService.updateReviewCache();
        System.out.println("refreshCache update");
        return ResponseEntity.ok().build();
    }

}

@Data
@AllArgsConstructor
class ReviewResponse {
    private Long movieId;
    private String review;
}
@Data
@AllArgsConstructor
class DeleteResponse {
    private Long movieId;
    private String message;
}
@Data
class ReviewRequest {
    private Long movieId;
    private String review;
}
