package com.example.movie_review.auth;

import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.movieDetail.DTO.MovieSearchDTO;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final DbRatingService dbRatingService;
    private final TmdbService tmdbService;

    @GetMapping("/home/realtime-data")
    public ResponseEntity<RealTimeData> getRealTimeData() {
        Long userCount = userService.getUserCount();
        Long totalRatings = dbRatingService.getTotalRatings();
        Long totalReviews = reviewService.getTotalReviews();

        RealTimeData data = new RealTimeData(userCount, totalRatings, totalReviews);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieSearchDTO>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page) throws JsonProcessingException {
        List<MovieSearchDTO> results = tmdbService.searchMovies(query, page);
        return ResponseEntity.ok(results);
    }

    /**
     * 후에 영화 리스트...
     */

}
