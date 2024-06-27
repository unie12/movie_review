package com.example.movie_review.repository;

import com.example.movie_review.domain.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Transactional
public interface GenresRepository extends JpaRepository<Genres, Long> {

    Optional<Genres> findByName(String name);
}
