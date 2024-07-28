package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByDbMoviesAndUser(DbMovies dbMovies, User user);

    List<Review> findByUser(User user);

    List<Review> findReviewByDbMovies(DbMovies dbMovies);

    @Query("SELECT new com.example.movie_review.review.ReviewWithHeartCount(r, SIZE(r.hearts)) " +
            "FROM Review r " +
            "WHERE r.dbMovies.movieDetails.id = :movieId " +
            "ORDER BY SIZE(r.hearts) DESC")
    Page<ReviewWithHeartCount> findByDbMovies_MovieDetails_IdWithHeartCount(@Param("movieId") Long movieId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE SIZE(r.hearts) > :heartCount ORDER BY SIZE(r.hearts) DESC")
    List<Review> findPopularReviews(@Param("heartCount") int heartCount);
}
