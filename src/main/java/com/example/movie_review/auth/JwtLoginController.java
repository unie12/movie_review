package com.example.movie_review.auth;

import com.example.movie_review.dbMovie.MovieCache;
import com.example.movie_review.dbMovie.MovieCacheRepository;
import com.example.movie_review.dbMovie.MovieCacheService;
import com.example.movie_review.dbMovie.MovieType;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginController {

    private final TmdbService tmdbService;
    private final MovieCacheService movieCacheService;

    private final MovieCacheRepository movieCacheRepository;

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
        String result = resultMono.block();
        // JSON 문자열을 모델에 추가
        model.addAttribute("searchResult", result);
        return "search";
    }
}