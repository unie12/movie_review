package com.example.movie_review.user.api;

import com.example.movie_review.movieDetail.MovieDetailDTOService;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserActivityController {
    private final UserService userService;
    private final MovieDetailDTOService movieDetailDTOService;


//    @GetMapping("/{userEmail}/favorite-movies")
//    public ResponseEntity<List<FavoriteMovieDTO>> getFavoriteMovies(
//            @PathVariable String userEmail,
//            @RequestParam(defaultValue = "favorite_date_desc") String sort) {
//
//        List<FavoriteMovieDTO> sortedMovies = userService.getSortedFavoriteMovies(userEmail, sort);
//        return ResponseEntity.ok(sortedMovies);
//    }
}
