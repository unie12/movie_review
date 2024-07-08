package com.example.movie_review.rating;

import lombok.Data;

@Data
public class RatingRequest {
    private Long userId;
    private Long movieId;
    private Double rating;
}
