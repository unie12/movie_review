package com.example.movie_review.recommend;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.movieDetail.service.MovieDetailService;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final UserService userService;
    private final MovieDetailService movieDetailService;

    @GetMapping("/content/{userEmail}")
    public ResponseEntity<List<MovieRecommendDTO>> getContentBasedRecommendations(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        List<MovieRecommendDTO> recommendations = recommendService.getContentBasedRecommendation(user);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/movies")
    @ResponseBody
    public Map<String, Object> getMoreMovies(@RequestParam int page) {
        List<MovieCommonDTO> movies = movieDetailService.getRandomMovies(page);
        boolean hasMore = movieDetailService.hasMoreMovies(page);

        Map<String, Object> response = new HashMap<>();
        response.put("movies", movies);
        response.put("hasMore", hasMore);

        return response;
    }

}
