package com.example.movie_review.movieDetail;

import lombok.Data;

import java.util.List;

@Data
public class ActorDetails {
    private List<Cast> cast;
    private List<Crew> crew;

    @Data
    public static class Cast {
        private boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private Integer id;
        private String original_language;
        private String original_title;
        private String overview;
        private double popularity;
        private String poster_path;
        private String release_date;
        private String title;
        private boolean video;
        private double vote_average;
        private Integer vote_count;
        private String character;
        private String credit_id;
        private Integer order;
        private String media_type;
    }

    @Data
    public static class Crew {
        private boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private Integer id;
        private String original_language;
        private String original_title;
        private String overview;
        private double popularity;
        private String poster_path;
        private String release_date;
        private String title;
        private boolean video;
        private double vote_average;
        private Integer vote_count;
        private String credit_id;
        private String department;
        private String job;
        private String media_type;
    }
}
