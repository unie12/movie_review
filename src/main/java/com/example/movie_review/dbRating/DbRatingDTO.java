package com.example.movie_review.dbRating;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DbRatingDTO {
    private Long id;
    private MovieCommonDTO movie;
    private Double score;
    private LocalDateTime ratingDate;
}
