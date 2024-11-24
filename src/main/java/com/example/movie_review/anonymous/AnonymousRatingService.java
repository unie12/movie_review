package com.example.movie_review.anonymous;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.recommend.MovieRecommendDTO;
import com.example.movie_review.recommend.RecommendRepository;
import com.example.movie_review.recommend.RecommendType;
import com.example.movie_review.recommend.RecommendedMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnonymousRatingService {

    private final AnonymousRatingRepository anonymousRatingRepository;
    private final RecommendRepository recommendRepository;
    private final AnonymousUserRepository anonymousUserRepository;

    /**
     * 평가하기 데이터 저장
     */
    @Transactional
    public void saveRatingsAndRecommendations(Map<String, Double> ratings, List<MovieRecommendDTO> recommendations, RecommendType type) {
        AnonymousUser anonymousUser = new AnonymousUser();

        ratings.forEach((movieId, rating) -> {
            AnonymousRating anonymousRating = new AnonymousRating();
            anonymousRating.setAnonymousUser(anonymousUser);
            anonymousRating.setRatedMovieId(Long.valueOf(movieId));
            anonymousRating.setRating(rating);
            anonymousUser.getRatings().add(anonymousRating);
        });

        recommendations.forEach(rec -> {
            RecommendedMovie recommendedMovie = new RecommendedMovie();
            recommendedMovie.setAnonymousUser(anonymousUser);
            recommendedMovie.setRecommendedMovieId(Long.valueOf(rec.getTmdbId()));
            recommendedMovie.setRecommendType(type);
            recommendedMovie.setSimilarity(rec.getSimilarity());
            recommendedMovie.setPoster_path(rec.getPoster_path());
            anonymousUser.getRecommendedMovies().add(recommendedMovie);
        });

        anonymousUserRepository.save(anonymousUser);
    }

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
        stats.put("recommendationsByType", recommendRepository.countByRecommendType());

        return stats;
    }
}
