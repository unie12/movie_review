package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.DTO.WeeklyUserDTO;
import com.example.movie_review.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("SELECT r FROM Review r ORDER BY r.uploadDate DESC")
    List<Review> findRecentReviews(int limit);

    @Query("SELECT r FROM Review r ORDER BY r.uploadDate DESC")
    Page<Review> findRecentReviewsWithPagination(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE SIZE(r.hearts) >= :minHeartCount")
    List<Review> findPopularReviewsWithMinHearts(@Param("minHeartCount") int minHeartCount);

    @Query("SELECT new com.example.movie_review.user.DTO.WeeklyUserDTO(new com.example.movie_review.user.DTO.UserCommonDTO(u.id, u.email, u.nickname, u.picture), COUNT(r)) " +
            "FROM Review r JOIN r.user u " +
            "WHERE r.uploadDate BETWEEN :startDate AND :endDate " +
            "GROUP BY u.id, u.email, u.nickname, u.picture " +
            "ORDER BY COUNT(r) DESC")
    List<WeeklyUserDTO> findTopReviewers(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(r) FROM Review r")
    Long getTotalReviews();
}
