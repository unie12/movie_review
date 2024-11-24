package com.example.movie_review.anonymous;

import com.example.movie_review.recommend.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnonymousRatingService {

    private final AnonymousRatingRepository anonymousRatingRepository;
    private final RecommendRepository recommendRepository;
    private final AnonymousUserRepository anonymousUserRepository;
    private final RecommendService recommendService;

    public Map<String, Object> getRatingStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("mostRatedMovies", anonymousRatingRepository.findMostRatedMovies(Limit.of(20)));
        stats.put("highestRatedMovies", anonymousRatingRepository.findHighestRatedMovies(Limit.of(20)));
        stats.put("totalRatings", anonymousRatingRepository.countTotalRatings());

        return stats;
    }

    public Map<String, Object> getRecommendationStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("mostRecommendedMovies", recommendRepository.findMostRecommendedMovies(Limit.of(20)));
//        stats.put("recommendationsByType", recommendRepository.countByRecommendType());

        return stats;
    }

    @Transactional
    public AnonymousUser saveUserAndRatings(Map<String, Double> ratings) {
        AnonymousUser anonymousUser = new AnonymousUser();

        ratings.forEach((movieId, rating) -> {
            AnonymousRating anonymousRating = new AnonymousRating();
            anonymousRating.setAnonymousUser(anonymousUser);
            anonymousRating.setRatedMovieId(Long.valueOf(movieId));
            anonymousRating.setRating(rating);
            anonymousUser.getRatings().add(anonymousRating);
        });

        return anonymousUserRepository.save(anonymousUser);
    }

    @Transactional(readOnly = true)
    public Map<String, List<MovieRecommendDTO>> getRecommendations(Map<String, Double> ratings) {
        List<MovieRecommendDTO> contentRecommendations = recommendService.getContentRecommendation(ratings);
        List<MovieRecommendDTO> hybridRecommendations = recommendService.getHybridRecommendation(ratings);

        Map<String, List<MovieRecommendDTO>> result = new HashMap<>();
        result.put("content", contentRecommendations);
        result.put("hybrid", hybridRecommendations);

        return result;
    }

    @Transactional
    public void saveRecommendationsForUser(AnonymousUser user, Map<String, List<MovieRecommendDTO>> recommendations) {
        processRecommendations(user, recommendations.get("content"), RecommendType.CONTENT_BASED);
        processRecommendations(user, recommendations.get("hybrid"), RecommendType.COLLABORATIVE_FILTERING);

        anonymousUserRepository.save(user);
    }

    private void processRecommendations(AnonymousUser user, List<MovieRecommendDTO> recommendations, RecommendType type) {
        recommendations.forEach(rec -> {
            RecommendedMovie recommendedMovie = new RecommendedMovie();
            recommendedMovie.setAnonymousUser(user);
            recommendedMovie.setRecommendedMovieId(Long.valueOf(rec.getTmdbId()));
            recommendedMovie.setRecommendType(type);
            recommendedMovie.setSimilarity(rec.getSimilarity());
            recommendedMovie.setPoster_path(rec.getPoster_path());
            user.getRecommendedMovies().add(recommendedMovie);
        });
    }

    public Map<String, List<MovieRecommendDTO>> saveRatingsAndGetRecommendations(Map<String, Double> ratings) {
        try {
            // 1. 사용자와 평가 저장
            AnonymousUser user = saveUserAndRatings(ratings);

            // 2. 추천 받기
            Map<String, List<MovieRecommendDTO>> recommendations = getRecommendations(ratings);

            // 3. 추천 결과 저장
            saveRecommendationsForUser(user, recommendations);

            return recommendations;
        } catch (Exception e) {
            log.error("Error in recommendation process", e);
            throw new RuntimeException("Failed to process recommendations", e);
        }
    }
}
