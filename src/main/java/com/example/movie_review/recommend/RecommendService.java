package com.example.movie_review.recommend;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {
    private final RestTemplate restTemplate;
    private final String RECOMMENDATION_API_URL = "http://3.36.54.11:8000/recommend";

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


}
