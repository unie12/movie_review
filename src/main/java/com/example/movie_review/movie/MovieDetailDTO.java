package com.example.movie_review.movie;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieDetailDTO {
    private int reviewCnt;
    private int ratingCnt;
    private Double ratingAvg;
}
