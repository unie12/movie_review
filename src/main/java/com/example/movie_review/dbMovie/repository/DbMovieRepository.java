package com.example.movie_review.dbMovie.repository;

import com.example.movie_review.dbMovie.DbMovies;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DbMovieRepository extends JpaRepository<DbMovies, Long> {

    Optional<DbMovies> findByTmdbId(Long tmdbId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM DbMovies m WHERE m.tmdbId = :tmdbId")
    Optional<DbMovies> findByTmdbIdWithLock(@Param("tmdbId") Long tmdbId);
}
