package com.example.movie_review.review.repository;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.review.Review;
import com.example.movie_review.user.DTO.WeeklyUserDTO;
import com.example.movie_review.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    /**
     * 특정 영화에 대한 특정 사용자의 리뷰 찾기
     */
    Optional<Review> findByDbMoviesAndUser(DbMovies dbMovies, User user);

    /**
     * 특정 영화에 대한 리뷰 전체 찾기
     */
    List<Review> findReviewByDbMovies(DbMovies dbMovies);

    @Query("SELECT r FROM Review r ORDER BY r.uploadDate DESC")
    Page<Review> findRecentReviewsWithPagination(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE SIZE(r.hearts) >= :minHeartCount AND r.uploadDate >= :startDate")
    List<Review> findPopularReviewsWithMinHearts(@Param("minHeartCount") int minHeartCount, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT r FROM Review r ORDER BY r.uploadDate DESC")
    List<Review> findRecentReviews();

    @Query("SELECT new com.example.movie_review.user.DTO.WeeklyUserDTO(" +
            "new com.example.movie_review.user.DTO.UserCommonDTO(u.id, u.email, u.nickname, u.picture, u.role), " +
            "COUNT(r), false) " +
            "FROM Review r JOIN r.user u " +
            "WHERE r.uploadDate BETWEEN :startDate AND :endDate " +
            "GROUP BY u.id, u.email, u.nickname, u.picture, u.role " +
            "ORDER BY COUNT(r) DESC")
    List<WeeklyUserDTO> findTopReviewers(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT r, " +
            "LOG10(GREATEST(1, SIZE(r.hearts))) + " +
            "(CASE WHEN TIMESTAMPDIFF(HOUR, r.uploadDate, CURRENT_TIMESTAMP) < 12 THEN 12.0 " +
            "      WHEN TIMESTAMPDIFF(HOUR, r.uploadDate, CURRENT_TIMESTAMP) < 24 THEN 6.0 " +
            "      WHEN TIMESTAMPDIFF(HOUR, r.uploadDate, CURRENT_TIMESTAMP) < 48 THEN 3.0 " +
            "      WHEN TIMESTAMPDIFF(HOUR, r.uploadDate, CURRENT_TIMESTAMP) < 72 THEN 1.5 " +
            "      ELSE 1.0 END) AS score " +
            "FROM Review r " +
            "ORDER BY score DESC")
    Page<Object[]> findPopularReviewsWithScore(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE SIZE(r.hearts) > :heartCount ORDER BY SIZE(r.hearts) DESC")
    List<Review> findPopularReviews(@Param("heartCount") int heartCount);

    @Query("SELECT COUNT(r) FROM Review r")
    Long getTotalReviews();
}
