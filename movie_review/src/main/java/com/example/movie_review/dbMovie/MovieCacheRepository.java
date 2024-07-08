package com.example.movie_review.dbMovie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieCacheRepository extends JpaRepository<MovieCache, Long> {

    Optional<MovieCache> findByType(MovieType movieType);
}
