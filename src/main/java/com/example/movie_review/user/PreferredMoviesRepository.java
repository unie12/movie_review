package com.example.movie_review.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferredMoviesRepository extends JpaRepository<PreferredMovies, Long> {
    List<PreferredMovies> findByUser(User user);

    boolean existsByUserAndMovieId(User user, String movieId);

    void deleteByUser(User user);
}
