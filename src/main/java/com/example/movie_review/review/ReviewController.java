package com.example.movie_review.review;

import lombok.AllArgsConstructor;
import lombok.Data;

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
