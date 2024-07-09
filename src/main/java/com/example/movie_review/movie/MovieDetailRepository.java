package com.example.movie_review.movie;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDetailRepository extends JpaRepository<MovieDetails, Long> {
}
