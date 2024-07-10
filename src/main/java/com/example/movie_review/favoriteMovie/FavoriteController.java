package com.example.movie_review.favoriteMovie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FavoriteController {
    private final UserFavoriteMovieService userFavoriteMovieService;

    @PostMapping("/favorite")
    public ResponseEntity<?> toggleFavorite(@RequestBody FavoriteRequest request, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FavoriteResponse(false, "User not authenticated", false));
        }

        try {
            String email = principal.getAttribute("email");
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