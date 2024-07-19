package com.example.movie_review.dbRating;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DbRatingDTO {
    private Long id;
    private MovieCommonDTO movie;
    private Double score;
}