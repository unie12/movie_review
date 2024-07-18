package com.example.movie_review.user.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteMovieDTO {
    private Long id;

    private String title;
    private String poster_path;
    private Long runtime;
    private String release_date;
    private Integer tId;
    private Double ajou_ratingAvg;
    private LocalDateTime favoritedAt;

}
