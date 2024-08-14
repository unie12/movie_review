package com.example.movie_review.dbRating;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.DTO.WeeklyUserDTO;
import com.example.movie_review.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DbRatingRepository extends JpaRepository<DbRatings, Long> {
    Optional<DbRatings> findByDbMoviesAndUser(DbMovies dbMovies, User user);

    Optional<DbRatings> findByUserIdAndDbMoviesId(Long userId, Long movieId);

    @Query("SELECT new com.example.movie_review.user.DTO.WeeklyUserDTO(" +
            "new com.example.movie_review.user.DTO.UserCommonDTO(u.id, u.email, u.nickname, u.picture, u.role), " +
            "COUNT(r), true) " +
            "FROM DbRatings r JOIN r.user u " +
            "WHERE r.uploadRating BETWEEN :startDate AND :endDate " +
            "GROUP BY u.id, u.email, u.nickname, u.picture, u.role " +
            "ORDER BY COUNT(r) DESC")
    List<WeeklyUserDTO> findTopRaters(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(r) FROM DbRatings r")
    Long getTotalRatings();

    void deleteByUser(User user);
}
