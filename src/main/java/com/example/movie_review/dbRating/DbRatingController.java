package com.example.movie_review.dbRating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rating")
public class DbRatingController {
    private final DbRatingService dbRatingService;

    @GetMapping("/{movieId}")
    public ResponseEntity<?> loadRating(@PathVariable Long movieId, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            String email = principal.getAttribute("email");
            Optional<DbRatings> dbRating = dbRatingService.getDbRating(email, movieId);

            return ResponseEntity.ok(new DbRatingResponse(movieId, dbRating.map(DbRatings::getScore).orElse(0.0), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 해당 영화에 대해 해당 유저의 평점 저장 또는 업데이트
     */
    @PostMapping("/{movieId}")
    public ResponseEntity<?> saveOrDeleteRating(@PathVariable Long movieId, @RequestBody DbRatingRequest request, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            String email = principal.getAttribute("email");
            Optional<DbRatings> dbRating = dbRatingService.getDbRating(email, movieId);

            if(dbRating.isPresent() && dbRating.get().getScore().equals(request.getRating())) {
                dbRatingService.deleteDbRating(movieId, email);
                return ResponseEntity.ok(new DbRatingResponse(movieId, 0.0, true));
            }
            else {
                DbRatings dbRatings = dbRatingService.saveOrUpdateRating(movieId, email, request.getRating());
                return ResponseEntity.ok(new DbRatingResponse(movieId, request.getRating(), false));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

@Data
class DbRatingRequest {
    private Long movieId;
    private Double rating;
}

@Data
@AllArgsConstructor
class DbRatingResponse {
    private Long movieId;
    private Double rating;
    private boolean deleted;
}

@Data
@AllArgsConstructor
class DbRatingDeleteResponse {
    private Long movieId;
    private boolean deleted;
}