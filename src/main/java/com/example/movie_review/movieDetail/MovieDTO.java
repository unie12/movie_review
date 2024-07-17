package com.example.movie_review.movieDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MovieDTO {
    private String id;
    private String title;
}