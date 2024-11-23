package com.example.movie_review.recommend;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.movieDetail.service.MovieDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {
    private final RestTemplate restTemplate;
    private final String CONTENT_RECOMMENDATION_URL = "http://3.36.54.11:8000/recommend/content";
    private final String HYBRID_RECOMMENDATION_URL = "http://3.36.54.11:8000/recommend/hybrid";
    private static final double MINIMUM_RATING = 3.5;
    private final MovieCommonDTOService movieCommonDTOService;
    private final DbMovieService dbMovieService;
    private final MovieDetailService movieDetailService;

    public List<MovieRecommendDTO> getContentRecommendation(Map<String, Double> ratingsMap) {
        return getRecommendations(ratingsMap, CONTENT_RECOMMENDATION_URL);
    }

    public List<MovieRecommendDTO> getHybridRecommendation(Map<String, Double> ratingsMap) {
        return getRecommendations(ratingsMap, HYBRID_RECOMMENDATION_URL);
    }

    // 공통 추천 로직
    private List<MovieRecommendDTO> getRecommendations(Map<String, Double> ratingsMap, String url) {
        List<String> movieIds = ratingsMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= MINIMUM_RATING)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (movieIds.isEmpty()) {
            log.warn("No movies rated above {} found", MINIMUM_RATING);
            return Collections.emptyList();
        }

        if (ratingsMap.isEmpty()) {
            log.warn("No ratings provided");
            return Collections.emptyList();
        }

        Map<String, Double> filteredRatings = ratingsMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= MINIMUM_RATING)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (filteredRatings.isEmpty()) {
            log.warn("No movies rated above {} found", MINIMUM_RATING);
            return Collections.emptyList();
        }

        // 요청 객체 생성
        Map<String, Object> request = new HashMap<>();
        request.put("tmdb_ids", new ArrayList<>(filteredRatings.keySet()));
        request.put("ratings", filteredRatings);  // 하이브리드 추천을 위한 평점 데이터 추가

        log.info("Sending recommendation request to {} with filtered movie IDs: {}", url, movieIds);

        ResponseEntity<List<MovieRecommendDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<List<MovieRecommendDTO>>() {}
        );

        return convertToMovieRecommendDTO(response.getBody(), url);
    }

    private List<MovieRecommendDTO> convertToMovieRecommendDTO(List<MovieRecommendDTO> results, String url) {
        List<MovieRecommendDTO> recommendations = new ArrayList<>();

        if (results != null) {
            for (MovieRecommendDTO result : results) {
                try {
                    MovieRecommendDTO dto = new MovieRecommendDTO();
                    dto.setTmdbId(result.getTmdbId());
                    dto.setTitle(result.getTitle());
                    dto.setRecommendation_type(result.getRecommendation_type());
                    dto.setRecommendedFrom(result.getRecommendedFrom());
                    dto.setSimilarity(result.getSimilarity());

                    // DB에서 추가 정보 조회
                    Optional<DbMovies> dbMovieOptional = dbMovieService.findByTmdbId(
                            Long.valueOf(result.getTmdbId())
                    );

                    if (dbMovieOptional.isPresent()) {
                        DbMovies dbMovie = dbMovieOptional.get();
                        MovieDetails details = dbMovie.getMovieDetails();

                        dto.setPoster_path(details != null ? details.getPoster_path() : "");
                        dto.setPopularity(result.getPopularity());
                        dto.setRelease_date(details != null ? details.getRelease_date() : null);
                        dto.setRuntime(details != null ? details.getRuntime() : null);
                        dto.setAjou_rating(dbMovie.getDbRatingAvg());
                        dto.setAjou_rating_cnt(dbMovie.getDbRatingCount());

                        // URL에 따라 다르게 처리
                        if (url.equals(CONTENT_RECOMMENDATION_URL)) {
                            // 컨텐츠 기반 추천의 경우 DB에 있는 영화만 포함
                            recommendations.add(dto);
                        } else if (url.equals(HYBRID_RECOMMENDATION_URL)) {
                            // 하이브리드 추천의 경우 모든 영화 포함
                            recommendations.add(dto);
                        }
                    } else {
                        // DB에 없는 경우
                        if (url.equals(HYBRID_RECOMMENDATION_URL)) {
                            DbMovies orCreateMovie = dbMovieService.findOrCreateMovie(Long.valueOf(result.getTmdbId()));
                                dto.setPoster_path(orCreateMovie.getMovieDetails().getPoster_path());
                                dto.setRelease_date(orCreateMovie.getMovieDetails().getRelease_date());

                            // 하이브리드 추천의 경우에만 DB에 없는 영화도 포함
                            dto.setPoster_path("");
                            dto.setPopularity(result.getPopularity());
                            dto.setRelease_date(null);
                            dto.setRuntime(null);
                            dto.setAjou_rating(0.0);
                            dto.setAjou_rating_cnt(0);
                            recommendations.add(dto);
                        }
                        // 컨텐츠 기반 추천의 경우 DB에 없는 영화는 제외
                    }

                } catch (Exception e) {
                    log.error("Error processing recommendation for TMDB ID {}: {}",
                            result.getTmdbId(), e.getMessage());
                }
            }
        }

        log.info("Converted {} recommendations for URL: {}", recommendations.size(), url);
        return recommendations;
    }
}
