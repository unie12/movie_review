package com.example.movie_review.auth;

import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.movieDetail.DTO.MovieSearchDTO;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.service.UserDTOService;
import com.example.movie_review.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final UserDTOService userDTOService;

    @GetMapping("/home/realtime-data")
    public ResponseEntity<RealTimeData> getRealTimeData() {
        Long userCount = userService.getUserCount();
        Long totalRatings = dbRatingService.getTotalRatings();
        Long totalReviews = reviewService.getTotalReviews();

        RealTimeData data = new RealTimeData(userCount, totalRatings, totalReviews);
        return ResponseEntity.ok(data);
    }

    // 일단 tmdbservice에서 size를 안먹으니까 10으로 직접 명시해서 처리중
    @GetMapping("/search/movies")
    public ResponseEntity<Page<MovieSearchDTO>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page) throws JsonProcessingException {
        Page<MovieSearchDTO> results = tmdbService.searchMovies(query, page);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/users")
    public ResponseEntity<Page<UserCommonDTO>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        Page<UserCommonDTO> results = userDTOService.searchUsers(query, page, size);
        return ResponseEntity.ok(results);
    }

    /**
     * 후에 영화 리스트...
     */

}
