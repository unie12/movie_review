package com.example.movie_review.movieDetail.DTO;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class RecommendMovies {
    private List<MovieCommonDTO> results;
    private int page;
    private int total_pages;
    private int total_results;
}
