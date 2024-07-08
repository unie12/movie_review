package com.example.movie_review.movie;

import com.example.movie_review.genre.Genres;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Builder
@Data @NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_details")
public class MovieDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long id;

    private String backdrop_path;
//    private Integer tId;
    private String title;
    private String original_title;
    private String name;
    private String overview;
    private String tagline;
    private String poster_path;

    private List<Genres> genres;
    private String release_date;
    private String first_air_date;
    private double vote_average;
    private Long vote_count;

    private Long runtime;
    private boolean adult;
    private String media_type;

    @OneToOne(mappedBy = "movieDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Movies movie;

    @OneToOne
    @JoinColumn(name = "credit_id")
    private Credits credits;

//    @Data
//    public static class Credits {
//        private List<Crew> crew;
//        private List<Cast> cast;
//
//        @Data
//        public static class Crew {
//            private boolean adult;
//            private Integer gender;
//            private Integer id;
//            private String known_for_department;
//            private String name;
//            private String original_name;
//            private double popularity;
//            private String profile_path;
//            private String credit_id;
//            private String department;
//            private String job;
//        }
//
//        @Data
//        public static class Cast {
//            private boolean adult;
//            private Integer gender;
//            private Integer id;
//            private String known_for_department;
//            private String name;
//            private String original_name;
//            private double popularity;
//            private String profile_path;
//            private Integer cast_id;
//            private String character;
//            private String credit_id;
//            private Integer order;
//        }
//    }


}
