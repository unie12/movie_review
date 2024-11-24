package com.example.movie_review.recommend;

import com.example.movie_review.anonymous.AnonymousRatingService;
import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.movieDetail.service.MovieDetailService;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
@Slf4j
public class RecommendController {
    private final RecommendService recommendService;
    private final UserService userService;
    private final MovieDetailService movieDetailService;
    private final AnonymousRatingService anonymousRatingService;

    @PostMapping("/recommendations")
    public ResponseEntity<Map<String, List<MovieRecommendDTO>>> getRecommendations(
            @RequestBody RecommendRequest request) {
        try {
            Map<String, List<MovieRecommendDTO>> recommendations =
                    anonymousRatingService.saveRatingsAndGetRecommendations(
                            request.getRatings()
                    );
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error while processing recommendations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PostMapping("/content/results")
//    public ResponseEntity<List<MovieRecommendDTO>> getContentRecommendations(
//            @RequestBody RecommendRequest request) {
//        List<MovieRecommendDTO> recommendations = recommendService.getContentRecommendation(request.getRatings());
//        anonymousRatingService.saveRatingsAndRecommendations(request.getRatings(), recommendations, RecommendType.CONTENT_BASED);
//        return ResponseEntity.ok(recommendations);
//    }
//
//    @PostMapping("/hybrid/results")
//    public ResponseEntity<List<MovieRecommendDTO>> getHybridRecommendations(
//            @RequestBody RecommendRequest request) {
//        List<MovieRecommendDTO> recommendations = recommendService.getHybridRecommendation(request.getRatings());
//        anonymousRatingService.saveRatingsAndRecommendations(request.getRatings(), recommendations, RecommendType.COLLABORATIVE_FILTERING);
//        return ResponseEntity.ok(recommendations);
//    }




    /**
     * 평가하기에 필요한 랜덤한 영화들 가져오기
     */
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
