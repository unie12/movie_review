package com.example.movie_review.favoriteMovie;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserFavoriteMovieRepository extends JpaRepository<UserFavoriteMovie, Long> {

    void deleteByUserAndDbMovie(User user, DbMovies dbMovies);

    Optional<UserFavoriteMovie> findByUserAndDbMovie(User user, DbMovies dbMovies);
}
