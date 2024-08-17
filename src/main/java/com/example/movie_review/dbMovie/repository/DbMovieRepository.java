package com.example.movie_review.dbMovie.repository;

import com.example.movie_review.dbMovie.DbMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DbMovieRepository extends JpaRepository<DbMovies, Long> {

    Optional<DbMovies> findByTmdbId(Long tmdbId);

    @Query("SELECT m FROM DbMovies m JOIN m.favoritedByUsers f WHERE f.favoriteDate >= :startDate")
    List<DbMovies> findDailyAjouPopularFavoriteMovies(@Param("startDate")LocalDateTime startDate);

    @Query("SELECT m FROM DbMovies m JOIN m.dbRatings r WHERE r.uploadRating >= :startDate")
    List<DbMovies> findDailyAjouPopularRatingMovies(@Param("startDate")LocalDateTime startDate);

    @Query("SELECT m FROM DbMovies m JOIN m.reviews r WHERE r.uploadDate >= :startDate")
    List<DbMovies> findDailyAjouPopularReviewMovies(@Param("startDate")LocalDateTime startDate);

}
