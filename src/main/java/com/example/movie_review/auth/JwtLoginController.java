package com.example.movie_review.auth;

import com.example.movie_review.heart.HeartService;
import com.example.movie_review.dbMovie.*;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.favoriteMovie.UserFavoriteMovieService;
import com.example.movie_review.movie.*;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.ReviewDTO;
import com.example.movie_review.review.ReviewRepository;
import com.example.movie_review.review.ReviewService;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginController {

    private final TmdbService tmdbService;
    private final DbMovieService dbMovieService;
    private final UserFavoriteMovieService userFavoriteMovieService;
    private final DbRatingService dbRatingService;
    private final HeartService heartService;
    private final ReviewService reviewService;
    private final MovieDetailDTOService movieDetailDTOService;

    private final MovieCacheRepository movieCacheRepository;
    private final ReviewRepository reviewRepository;

    private final ObjectMapper objectMapper;
    @GetMapping({"", "/"})
    public String home(Model model, Authentication auth) throws JsonProcessingException {
//        movieCacheService.updateDailyMovieCache();
//        movieCacheService.updateWeeklyMovieCache();

        MovieCache dailyCache = movieCacheRepository.findByType(MovieType.DBOM)
                .orElseThrow(() -> new RuntimeException("Daily cache not found"));
        MovieCache weeklyCache = movieCacheRepository.findByType(MovieType.WBOM)
                .orElseThrow(() -> new RuntimeException("Weekly cache not found"));
        MovieCache popularCache = movieCacheRepository.findByType(MovieType.POPULAR)
                .orElseThrow(() -> new RuntimeException("Popular cache not found"));
        MovieCache trendingCache = movieCacheRepository.findByType(MovieType.TRENDING)
                .orElseThrow(() -> new RuntimeException("Trending cache not found"));

        model.addAttribute("popularMovies", popularCache.getMovieData());
        model.addAttribute("trendingMovies", trendingCache.getMovieData());
        model.addAttribute("dBOM", dailyCache.getMovieData());
        model.addAttribute("wBOM", weeklyCache.getMovieData());

        return "home";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam String query, Model model) {

        Mono<String> resultMono = tmdbService.searchMovies(query);
        String result = resultMono.block(); // Mono를 동기적으로 처리 (주의: 프로덕션 환경에서는 비동기 처리 권장)
        // JSON 문자열을 모델에 추가
        model.addAttribute("searchResult", result);
        return "search";
    }

    /**
     * 영화 상세 정보 보여주기
     * 해당 영화의 상세 정보를 가저 오면서 db에 해당 영화가 존재하는 지 확인
     * 만약 존재하지 않으면 db에 추가
     * 존재하면 db에 있는 거 그대로 반환
     */
    @GetMapping("/contents/{movieId}")
    public String movieDetail(@PathVariable Long movieId, Model model, @AuthenticationPrincipal OAuth2User principal) {
        try {
            DbMovies dbMovie = dbMovieService.findOrCreateMovie(movieId);
            MovieDetails movieDetails = dbMovie.getMovieDetails();
            List<Crew> directors = dbMovieService.getDirectors(movieDetails);
            List<Review> reviews = reviewRepository.findReviewByDbMovies(dbMovie);
            List<ReviewDTO> reviewDTOS = reviewService.getReviewDTOs(reviews);

            MovieDetailDTO movieDetailDTO = movieDetailDTOService.getMovieDetailDTO(movieId, principal);

            model.addAttribute("dbMovie", dbMovie);
            model.addAttribute("movieDetails", movieDetails);
            model.addAttribute("directors", directors);
            model.addAttribute("reviews", reviews);
            model.addAttribute("reviewDTOs", reviewDTOS);

            model.addAttribute("movieDTO", movieDetailDTO);
            System.out.println("movieDetailDTO.getId() = " + movieDetailDTO.getId());
            System.out.println("dbMovie.getId() = " + dbMovie.getId());





            if (principal != null) {
                String email = principal.getAttribute("email");
//                model.addAttribute("isFavorite", userFavoriteMovieService.isFavorite(email, movieDetails.getId()));
                model.addAttribute("userRating", dbRatingService.getDbRating(email, movieDetails.getId()).orElse(new DbRatings()));
                model.addAttribute("userHearts", heartService.getUserHearts(email));
            } else {
//                model.addAttribute("isFavorite", false);
                model.addAttribute("userRating", new DbRatings());
                model.addAttribute("userHearts", Collections.emptyList());
            }
        } catch (Exception e) {
            log.error("Error processing movie details", e);
            model.addAttribute("error", "영화 정보를 처리하는 중 오류가 발생했습니다.");
        }

        return "movieDetail";
    }

    @GetMapping("/people/{actorId}")
    public String actorDetail(@PathVariable Long actorId, Model model) {
        String actorDetailsJson = tmdbService.getActorDetails(actorId).block();

        // popularity sort, media_type = movie만 일단
        try {
            if (actorDetailsJson == null || actorDetailsJson.isEmpty()) {
                throw new Exception("Empty or null JSON data");
            }
            ActorDetails actorDetails = objectMapper.readValue(actorDetailsJson, ActorDetails.class);

            List<ActorDetails.Cast> sortedCast = actorDetails.getCast().stream()
                            .filter(cast -> "movie".equals(cast.getMedia_type()))
                            .sorted((cast1, cast2) -> Double.compare(cast2.getPopularity(), cast1.getPopularity()))
                            .collect(Collectors.toList());
            actorDetails.setCast(sortedCast);
            model.addAttribute("actorDetails", actorDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "actorDetail";
    }
}