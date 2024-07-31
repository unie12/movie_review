package com.example.movie_review.movieDetail;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieBasicInfo {

    private Long id;
    private Long tId;
    private String title;
    private String original_title;
    private String backdrop_path;
    private String release_date;
    private List<String> genres;
    private Long runtime;
    private String poster_path;
    private String tagline;
    private String overview;
    private List<DirectorDTO> directors;
    private List<ActorDTO> actors;

    private String watchProvider;

}
