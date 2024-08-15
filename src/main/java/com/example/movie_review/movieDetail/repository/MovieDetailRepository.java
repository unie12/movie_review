package com.example.movie_review.movieDetail.repository;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface MovieDetailRepository extends JpaRepository<MovieDetails, Long> {
    List<MovieDetails> findTop10ByTitleContainingIgnoreCase(String title);

    MovieDetails findBytId(Long tId);
}
