package com.example.movie_review.dbMovie.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MoviePopularityDTO {
    private String poster_path;
    private String title;

    private Long favoriteCount;
    private Long ratingCount;
    private Long reviewCount;

    private Double avgRating;
    private Long totalRatingCount;

    public MoviePopularityDTO(String poster_path, String title, Long favoriteCount, Long ratingCount, Long reviewCount, Double avgRating, Long totalRatingCount) {
        this.poster_path = poster_path;
        this.title = title;
        this.favoriteCount = favoriteCount;
        this.ratingCount = ratingCount;
        this.reviewCount = reviewCount;
        this.avgRating = avgRating;
        this.totalRatingCount = totalRatingCount;
    }
}
