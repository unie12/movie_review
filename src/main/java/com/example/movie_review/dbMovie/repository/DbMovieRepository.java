package com.example.movie_review.dbMovie.repository;

import com.example.movie_review.dbMovie.DbMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DbMovieRepository extends JpaRepository<DbMovies, Long> {

    Optional<DbMovies> findByTmdbId(Long tmdbId);
}
