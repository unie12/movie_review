package com.example.movie_review.recommend;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.movieDetail.service.MovieDetailService;
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
//    private final AnonymousRatingService anonymousRatingService;

    @PostMapping("/content/results")
    public ResponseEntity<List<MovieRecommendDTO>> getContentRecommendations(
            @RequestBody RecommendRequest request) {
        List<MovieRecommendDTO> recommendations = recommendService.getContentRecommendation(request.getRatings());
//        anonymousRatingService.saveRatingsAndRecommendations(request.getRatings(), recommendations);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/hybrid/results")
    public ResponseEntity<List<MovieRecommendDTO>> getHybridRecommendations(
            @RequestBody RecommendRequest request) {
        List<MovieRecommendDTO> recommendations = recommendService.getHybridRecommendation(request.getRatings());
//        anonymousRatingService.saveRatingsAndRecommendations(request.getRatings(), recommendations);
        return ResponseEntity.ok(recommendations);
    }

    /**
     * 로그인한 사용자의 인생 영화를 토대로 컨텐츠 추천 결과 반환
     */
//    @GetMapping("/content/{userEmail}")
//    public ResponseEntity<List<MovieRecommendDTO>> getContentBasedRecommendations(@PathVariable String userEmail) {
//        User user = userService.getUserByEmail(userEmail);
//        List<MovieRecommendDTO> recommendations = recommendService.getContentBasedRecommendation(user);
//        return ResponseEntity.ok(recommendations);
//    }

    /**
     * 익명의 사용자가 평가한 영화들을 바탕으로 컨텐츠 기반 추천 결과 반환
     */
//    @PostMapping("/content/results")
//    public ResponseEntity<List<MovieCommonDTO>> getContentRecommendations(
//            @RequestBody Map<String, Double> ratingsMap) {
//        List<MovieCommonDTO> recommendations = recommendService.getContentRecommendation(ratingsMap);
//        anonymousRatingService.saveRatingsAndRecommendations(ratingsMap, recommendations);
//
//        return ResponseEntity.ok(recommendations);
//    }

//    @PostMapping("/hybrid/results")
//    public ResponseEntity<List<MovieCommonDTO>> getHybridRecommendations(
//            @RequestBody Map<String, Double> ratingsMap) {
//        List<MovieCommonDTO> recommendations = recommendService.getHybridRecommendation(ratingsMap);
//        anonymousRatingService.saveRatingsAndRecommendations(ratingsMap, recommendations);
//
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

    /**
     * 현재 평가하기 데이터 통계 값 반환
     * 1. 어떤 영화 많이 평가했는지
     * 2. 평점 높은 영화
     * 3. 어떤 영화들이 추천이 많이 됐는지
     */
    @GetMapping("/analytics")
    public Map<String, Object> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        return analytics;
    }

}
