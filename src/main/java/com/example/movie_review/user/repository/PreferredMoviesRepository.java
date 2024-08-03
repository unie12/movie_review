package com.example.movie_review.user.repository;

import com.example.movie_review.user.domain.PreferredMovies;
import com.example.movie_review.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferredMoviesRepository extends JpaRepository<PreferredMovies, Long> {
    List<PreferredMovies> findByUser(User user);

    boolean existsByUserAndMovieId(User user, String movieId);

}
