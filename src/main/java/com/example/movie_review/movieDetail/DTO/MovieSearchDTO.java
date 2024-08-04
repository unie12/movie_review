package com.example.movie_review.movieDetail.DTO;

import lombok.Data;

@Data
public class MovieSearchDTO {
    private Long Id;
    private String title;
    private String poster_path;
    private String release_date;
    private Double vote_average;
    private int vote_count;

    public MovieSearchDTO(Long id, String title, String poster_path, String release_date, Double vote_average, int vote_count) {
        Id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }
}
