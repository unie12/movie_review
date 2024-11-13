package com.example.movie_review.recommend;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.movieDetail.service.MovieDetailService;
import com.example.movie_review.user.domain.PreferredMovies;
import com.example.movie_review.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {
    private final RestTemplate restTemplate;
    private final String RECOMMENDATION_API_URL = "http://3.36.54.11:8000/recommend";
    private static final double MINIMUM_RATING = 4.0;
    private final MovieCommonDTOService movieCommonDTOService;
    private final DbMovieService dbMovieService;
    private final MovieDetailService movieDetailService;

    public List<MovieRecommendDTO> getContentBasedRecommendation(User user) {
        List<PreferredMovies> preferredMovies = user.getPreferredMovies();

        List<String> movieIds = preferredMovies.stream()
                .map(PreferredMovies::getMovieId)
                .collect(Collectors.toList());

        log.info("사용자 선호 영화 아이디 리스트: {}", movieIds);

        Map<String, List<String>> request = new HashMap<>();
        request.put("tmdb_ids", movieIds);

        ResponseEntity<List<MovieRecommendDTO>> response = restTemplate.exchange(
                RECOMMENDATION_API_URL,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<List<MovieRecommendDTO>>() {}
        );

        List<MovieRecommendDTO> recommendations = response.getBody();

        // 상세 로깅 추가
        if (recommendations != null) {
            recommendations.forEach(movie ->
                    log.info("추천 영화: tmdbId={}, title={}, posterPath={}, popularity={}",
                            movie.getTmdbId(),
                            movie.getTitle(),
                            movie.getPoster_path(),
                            movie.getPopularity()
                    )
            );
        }

        return recommendations;
    }


//    public List<MovieCommonDTO> getContentRecommendation(Map<String, Double> ratingsMap) {
//        List<String> movieIds = ratingsMap.entrySet().stream()
//                .filter(entry -> entry.getValue() >= MINIMUM_RATING)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//
//        if (movieIds.isEmpty()) {
//            log.warn("No movies rated above {} found", MINIMUM_RATING);
//            return Collections.emptyList();
//        }
//
//        Map<String, List<String>> request = new HashMap<>();
//        request.put("tmdb_ids", movieIds);
//
//        log.info("Sending recommendation request with filtered movie IDs: {}", movieIds);
//
//        ResponseEntity<List<MovieRecommendDTO>> response = restTemplate.exchange(
//                RECOMMENDATION_API_URL,
//                HttpMethod.POST,
//                new HttpEntity<>(request),
//                new ParameterizedTypeReference<List<MovieRecommendDTO>>() {
//                }
//        );
//
//
//        List<MovieRecommendDTO> results = response.getBody();
//        if (results != null) {
//            List<MovieCommonDTO> recommendations = results.stream()
//                    .map(m -> {
//                        DbMovies dbMovie = dbMovieService.findOrCreateMovie(Long.valueOf(m.getTmdbId()));
//                        return movieCommonDTOService.getMovieCommonDTO(dbMovie, dbMovie.getMovieDetails());
//                    }).toList();
//            return recommendations;
//        }
//        return null;
//    }

    public List<MovieCommonDTO> getContentRecommendation(Map<String, Double> ratingsMap) {
        List<String> movieIds = ratingsMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= MINIMUM_RATING)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (movieIds.isEmpty()) {
            log.warn("No movies rated above {} found", MINIMUM_RATING);
            return Collections.emptyList();
        }

        Map<String, List<String>> request = new HashMap<>();
        request.put("tmdb_ids", movieIds);

        log.info("Sending recommendation request with filtered movie IDs: {}", movieIds);

        ResponseEntity<List<MovieRecommendDTO>> response = restTemplate.exchange(
                RECOMMENDATION_API_URL,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<List<MovieRecommendDTO>>() {
                }
        );

        List<MovieRecommendDTO> results = response.getBody();
        List<MovieCommonDTO> recommendations = new ArrayList<>();

        if (results != null) {
            for (MovieRecommendDTO movieRecommendDTO : results) {
                Optional<DbMovies> dbMovieOptional = dbMovieService.findByTmdbId(Long.valueOf(movieRecommendDTO.getTmdbId()));
                if (dbMovieOptional.isPresent()) {
                    DbMovies dbMovie = dbMovieOptional.get();
                    MovieCommonDTO movieCommonDTO = movieCommonDTOService.getMovieCommonDTO(dbMovie, dbMovie.getMovieDetails());
                    recommendations.add(movieCommonDTO);
                } else {
                    log.debug("Skipping movie recommendation with TMDB ID: {}", movieRecommendDTO.getTmdbId());
                }
            }
        }

        return recommendations;
    }
//
//        if (recommendations != null) {
//            recommendations.forEach(movie ->
//                    log.info("추천 영화: tmdbId={}, title={}, posterPath={}, popularity={}",
//                            movie.getTmdbId(),
//                            movie.getTitle(),
//                            movie.getPoster_path(),
//                            movie.getPopularity()
//                    )
//            );
//        }


}
