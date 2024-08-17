package com.example.movie_review.auth;

import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.movieDetail.DTO.MovieSearchDTO;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.DTO.UserSearchDTO;
import com.example.movie_review.user.service.UserDTOService;
import com.example.movie_review.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final DbRatingService dbRatingService;
    private final TmdbService tmdbService;
    private final UserDTOService userDTOService;
    private final MovieCommonDTOService movieCommonDTOService;

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
    public ResponseEntity<Page<UserSearchDTO>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) throws JsonProcessingException {
        String currentEmail = auth != null ? auth.getName() : null;
        Page<UserSearchDTO> results = userDTOService.searchUsers(query, page, size, currentEmail);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/suggestions")
    @ResponseBody
    public ResponseEntity<List<String>> getSuggestions(
            @RequestParam String query,
            @RequestParam String searchType) {
        List<String> suggestions;

        if (searchType.equals("movieTitle")) {
            suggestions = movieCommonDTOService.getMovieTitleSuggestions(query);
        } else if (searchType.equals("userNickname")) {
            suggestions = userDTOService.getUserNicknameSuggestions(query);
        } else {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("suggestions = " + suggestions);
        // 일단 10개로 제한은 해놓을게
        return ResponseEntity.ok(suggestions.stream().limit(10).collect(Collectors.toList()));
    }

    /**
     * 후에 영화 리스트...
     */

}
