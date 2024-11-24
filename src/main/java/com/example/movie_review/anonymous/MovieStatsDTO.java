package com.example.movie_review.anonymous;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieStatsDTO {
    private Long movieId;
    private Long count;
    private Double averageRating;
    private String poster_path;

    // 추천 영화용 생성자
    public MovieStatsDTO(Long movieId, Long count, String poster_path) {
        this.movieId = movieId;
        this.count = count;
        this.averageRating = null;
        this.poster_path = poster_path;
    }
}