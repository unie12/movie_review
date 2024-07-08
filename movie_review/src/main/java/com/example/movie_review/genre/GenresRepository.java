package com.example.movie_review.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface GenresRepository extends JpaRepository<Genres, Long> {

    Optional<Genres> findByName(String name);
}
