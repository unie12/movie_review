package com.example.movie_review.recommend;

import com.example.movie_review.anonymous.MovieStatsDTO;
import com.example.movie_review.user.domain.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface RecommendRepository extends JpaRepository<RecommendedMovie, Long> {
    @Query("SELECT new com.example.movie_review.anonymous.MovieStatsDTO(r.recommendedMovieId, COUNT(r), 0.0, r.poster_path) " +
            "FROM RecommendedMovie r " +
            "GROUP BY r.recommendedMovieId " +
            "ORDER BY COUNT(r) DESC")
    List<MovieStatsDTO> findMostRecommendedMovies(Limit limit);

    @Query("SELECT r.recommendType, COUNT(r) " +
            "FROM RecommendedMovie r " +
            "GROUP BY r.recommendType")
    Map<RecommendType, Long> countByRecommendType();
}