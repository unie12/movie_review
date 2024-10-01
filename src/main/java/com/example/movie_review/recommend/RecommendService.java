package com.example.movie_review.recommend;

import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.movieLens.RecommendationDto;
import com.example.movie_review.user.domain.PreferredMovies;
import com.example.movie_review.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {
    private final RestTemplate restTemplate;
    private final String RECOMMENDATION_API_URL = "http://43.200.40.13:8000/recommend";

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

        log.info("컨텐츠 기반 사용자 추천 영화 리스트 {}", response.getBody());

        return response.getBody();
    }
}
