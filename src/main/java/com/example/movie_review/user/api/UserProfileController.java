package com.example.movie_review.user.api;

import com.example.movie_review.genre.PreferredGenres;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.DTO.UserProfileDTO;
import com.example.movie_review.user.DTO.UserProfileDTOService;
import com.example.movie_review.user.DTO.UserProfileUpdateRequest;
import com.example.movie_review.user.PreferredMovies;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/additional-info/{userEmail}")
public class UserProfileController {
    private final TmdbService tmdbService;
    private final UserProfileDTOService userProfileDTOService;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String userEmail) {
        return ResponseEntity.ok(userProfileDTOService.getUserProfileDTO(userEmail));
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable String userEmail, @RequestBody UserProfileUpdateRequest updateRequest) {
        return ResponseEntity.ok(userProfileDTOService.updateUserProfile(userEmail, updateRequest));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameAvailability(@RequestParam String nickname) {
        return ResponseEntity.ok(userProfileDTOService.isNicknameAvailable(nickname));
    }

    @GetMapping("/search-movies")
    public Mono<ResponseEntity<String>> searchMovies(@RequestParam String query) {
        return tmdbService.searchMovies(query)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/favorite-movies")
    public ResponseEntity<List<PreferredMovies>> getFavoriteMovies(@PathVariable String userEmail) {
        return ResponseEntity.ok(userProfileDTOService.getFavoriteMovies(userEmail));
    }

    @GetMapping("/favorite-genres")
    public ResponseEntity<List<PreferredGenres>> getFavoriteGenres(@PathVariable String userEmail) {
        return ResponseEntity.ok(userProfileDTOService.getFavoriteGenres(userEmail));
    }

}
