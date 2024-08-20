package com.example.movie_review.movieDetail.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MovieUiDTO {
    private Long id; // tmdb id
    private String title;
    private String poster_path;

    public MovieUiDTO(Long id, String title, String poster_path) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
    }
}
