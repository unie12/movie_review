package com.example.movie_review.user.api;

import com.example.movie_review.movieDetail.MovieDTO;
import com.example.movie_review.movieDetail.MovieDetailDTO;
import com.example.movie_review.movieDetail.MovieDetailDTOService;
import com.example.movie_review.user.DTO.FavoriteMovieDTO;
import com.example.movie_review.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
