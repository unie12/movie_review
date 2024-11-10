package com.example.movie_review.recommend;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.movieDetail.service.MovieDetailService;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @GetMapping("/content/results")
    public ResponseEntity<List<MovieRecommendDTO>> getContentRecommendations(
            @RequestParam String ratings) {
        // JSON 문자열을 Map으로 변환
        Map<String, Double> ratingsMap;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ratingsMap = mapper.readValue(ratings, new TypeReference<Map<String, Double>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid ratings format", e);
        }

        List<MovieRecommendDTO> recommendations = recommendService.getContentRecommendation(ratingsMap);
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
