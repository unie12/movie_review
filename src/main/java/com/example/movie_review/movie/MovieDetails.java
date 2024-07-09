package com.example.movie_review.movie;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.genre.Genres;
import jakarta.persistence.*;
import lombok.*;

import java.lang.invoke.CallSite;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credits_id")
    private Credits credits;

//    @OneToMany(mappedBy = "movieDetails", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MovieGenre> movieGenres;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movieDetails_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genres> genres = new HashSet<>();

    @OneToOne(mappedBy = "movieDetails", cascade = CascadeType.ALL)
    private DbMovies dbMovie;

    @Transient
    private List<GenreDto> genreDtos;


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
