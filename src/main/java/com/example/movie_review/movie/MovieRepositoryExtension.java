package com.example.movie_review.movie;

import com.example.movie_review.genre.Genres;

import java.util.List;
import java.util.Set;

public interface MovieRepositoryExtension {
    List<Movies> findByGenres(Set<Genres> genres, RecommendationDto recommendationDto);
}
