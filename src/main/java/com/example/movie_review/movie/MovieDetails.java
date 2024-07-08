package com.example.movie_review.movie;

import com.example.movie_review.genre.Genres;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_detail")
public class MovieDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movieDetails_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "credits_id")
    private Credits credits;

    @OneToMany(mappedBy = "movieDetails")
    private List<Genres> genres;

    private String backdrop_path;
    private Integer tId;
    private String title;
    private String original_title;
    private String name;
    private String overview;
    private String tagline;
    private String poster_path;

    private String release_date;
    private String first_air_date;
    private double vote_average;
    private Long vote_count;

    private Long runtime;
    private boolean adult;
    private String media_type;
}
