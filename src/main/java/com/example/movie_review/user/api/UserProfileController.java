package com.example.movie_review.user.api;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.genre.PreferredGenres;
import com.example.movie_review.movieDetail.DTO.MovieDTO;
import com.example.movie_review.movieDetail.service.MovieDetailDTOService;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.DTO.UserProfileDTO;
import com.example.movie_review.user.DTO.UserProfileUpdateRequest;
import com.example.movie_review.user.domain.PreferredMovies;
import com.example.movie_review.user.service.PreferredMoviesService;
import com.example.movie_review.user.service.UserProfileDTOService;
import com.example.movie_review.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/additional-info/{userEmail}")
public class UserProfileController {
    private final TmdbService tmdbService;
    private final UserProfileDTOService userProfileDTOService;
    private final PreferredMoviesService preferredMoviesService;
    private final UserService userService;
    private final DbMovieService dbMovieService;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String userEmail) {
        return ResponseEntity.ok(userProfileDTOService.getUserProfileDTO(userEmail));
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateUserProfile(
            @PathVariable String userEmail,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart("updateRequest") UserProfileUpdateRequest updateRequest) {
        // 인생 영화에 추가하면 영화 db에도 저장하기
        List<DbMovies> collect = updateRequest.getFavoriteMovies().stream()
                .map(movie -> dbMovieService.findOrCreateMovie(Long.valueOf(movie.getId())))
                .collect(Collectors.toList());
        System.out.println("collect = " + collect);
        return ResponseEntity.ok(userProfileDTOService.updateUserProfile(userEmail, profilePicture, updateRequest));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameAvailability(@RequestParam String nickname) {
        return ResponseEntity.ok(userProfileDTOService.isNicknameAvailable(nickname));
    }

//    @GetMapping("/search-movies")
//    public Mono<ResponseEntity<String>> searchMovies(@RequestParam String query) {
//        return tmdbService.searchMovies(query)
//                .map(ResponseEntity::ok);
//    }

    @GetMapping("/favorite-movies")
    public ResponseEntity<List<PreferredMovies>> getFavoriteMovies(@PathVariable String userEmail) {
        return ResponseEntity.ok(preferredMoviesService.getPreferredMoviesByUser(userService.getUserByEmail(userEmail)));
    }

    @GetMapping("/favorite-genres")
    public ResponseEntity<List<PreferredGenres>> getFavoriteGenres(@PathVariable String userEmail) {
        return ResponseEntity.ok(userProfileDTOService.getFavoriteGenres(userEmail));
    }

}
