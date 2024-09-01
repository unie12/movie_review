package com.example.movie_review.dbMovie.repository;

import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface DbMovieRepositoryCustom {
    List<MoviePopularityDTO> findPopularMoviesByUserGroup(List<Long> userIds, LocalDateTime startDate, double minRating);
}
