package com.example.movie_review.movieDetail.repository;

import com.example.movie_review.movieDetail.domain.MovieDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDetailRepository extends JpaRepository<MovieDetails, Long> {
}
