package com.example.movie_review.favoriteMovie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie/favorite")
public class FavoriteController {
    private final UserFavoriteMovieService userFavoriteMovieService;

    /**
     * request의 영화에 대해 현재 사용자의 찜 기능 토글 활성화
     */
    @PostMapping("")
    public ResponseEntity<?> toggleFavorite(@RequestBody FavoriteRequest request, Authentication principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FavoriteResponse(false, "User not authenticated", false));
        }

        try {
            String email = principal.getName();
            boolean isFavorite = userFavoriteMovieService.toggleFavorite(email, request.getMovieId(), request.isFavorite());
            return ResponseEntity.ok(new FavoriteResponse(true, "Favorite toggled successfully", isFavorite));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new FavoriteResponse(false, e.getMessage(), false));
        }
    }
}

@Data
class FavoriteRequest {
    private Long movieId;
    private boolean favorite;
}

@Data
@AllArgsConstructor
class FavoriteResponse {
    private boolean success;
    private String message;
    private boolean isFavorite;
}