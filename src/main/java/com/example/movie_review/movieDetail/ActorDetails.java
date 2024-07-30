package com.example.movie_review.movieDetail;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActorDetails {
    private List<Cast> cast;
    private List<Crew> crew;

    @Data
    public static class Cast {
        private String media_type;
        private double popularity;
        private String release_date;
        private String poster_path;
        private String original_title;
        private String title;
        private Integer id;
        private String character;
        private Integer vote_count;
        private double vote_average;
    }

    @Data
    public static class Crew {
        private String original_title;
        private String title;
        private String poster_path;
        private Integer id;
        private double popularity;
        private String release_date;
        private String department;
        private String media_type;
        private String job;
        private double vote_average;
        private Integer vote_count;

//        private boolean adult;
//        private String backdrop_path;
//        private List<Integer> genre_ids;
//        private String original_language;
//        private String overview;
//        private boolean video;
//        private String credit_id;
    }
}
