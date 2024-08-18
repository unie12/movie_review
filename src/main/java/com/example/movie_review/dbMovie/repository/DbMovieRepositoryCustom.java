package com.example.movie_review.dbMovie.repository;

import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface DbMovieRepositoryCustom {
    List<MoviePopularityDTO> findAjouPopularMovies(LocalDateTime startDate, double minRating);
}