package com.example.movie_review.movieDetail.domain;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.genre.Genres;
import com.example.movie_review.movieDetail.DTO.GenreDto;
import com.example.movie_review.movieDetail.domain.Credits;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
//@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_detail")
public class MovieDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movieDetails_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "credits_id")
    private Credits credits;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movieDetails_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genres> genres = new HashSet<>();


    @OneToOne(mappedBy = "movieDetails")
    private DbMovies dbMovie;

    @OneToMany(mappedBy = "movieDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovieKeyword> movieKeywords = new HashSet<>();

    @Transient
    private List<GenreDto> genreDtos;


    private String backdrop_path;
    @Column
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


    public void addMovieKeyword(MovieKeyword movieKeyword) {
        movieKeywords.add(movieKeyword);
        movieKeyword.setMovieDetails(this);
    }

}
