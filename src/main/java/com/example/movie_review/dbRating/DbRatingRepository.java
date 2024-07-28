package com.example.movie_review.dbRating;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DbRatingRepository extends JpaRepository<DbRatings, Long> {
    Optional<DbRatings> findByDbMoviesAndUser(DbMovies dbMovies, User user);

    Optional<DbRatings> findByUserIdAndDbMoviesId(Long userId, Long movieId);
}
