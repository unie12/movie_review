package com.example.movie_review.dbMovie;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieCommonDTO {
    private Long id;
    private Long tId;
    private String title;
    private String poster_path;
}
