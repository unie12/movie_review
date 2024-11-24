package com.example.movie_review.anonymous;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnonymousRatingRepository extends JpaRepository<AnonymousRating, Long> {
    @Query("SELECT new com.example.movie_review.anonymous.MovieStatsDTO(" +
            "ar.ratedMovieId, COUNT(ar), AVG(ar.rating), md.poster_path) " +
            "FROM AnonymousRating ar " +
            "LEFT JOIN MovieDetails md ON ar.ratedMovieId = md.tId " +
            "GROUP BY ar.ratedMovieId, md.poster_path " +
            "ORDER BY COUNT(ar) DESC")
    List<MovieStatsDTO> findMostRatedMovies(Limit limit);

    @Query("SELECT new com.example.movie_review.anonymous.MovieStatsDTO(" +
            "ar.ratedMovieId, COUNT(ar), AVG(ar.rating), md.poster_path) " +
            "FROM AnonymousRating ar " +
            "LEFT JOIN MovieDetails md ON ar.ratedMovieId = md.tId " +
            "GROUP BY ar.ratedMovieId, md.poster_path " +
            "HAVING COUNT(ar) >= 3 " +
            "ORDER BY AVG(ar.rating) DESC")
    List<MovieStatsDTO> findHighestRatedMovies(Limit limit);

    @Query("SELECT COUNT(ar) FROM AnonymousRating ar")
    long countTotalRatings();
}