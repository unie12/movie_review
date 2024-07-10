package com.example.movie_review.dbMovie;

import com.example.movie_review.domain.review.Review;
import com.example.movie_review.favoriteMovie.UserFavoriteMovie;
import com.example.movie_review.movie.MovieDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity @Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DbMovies")
public class DbMovies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DbMovies_id")
    private Long id;

    @Column(name = "tmdb_id", unique = true)
    private Long tmdbId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "movieDetails_id")
    private MovieDetails movieDetails;

    @OneToMany(mappedBy = "dbMovies", cascade = CascadeType.ALL)
    private List<DbRatings> dbRatings;

    @OneToMany(mappedBy = "dbMovie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFavoriteMovie> favoritedByUsers = new ArrayList<>();

    @OneToMany(mappedBy = "dbMovies", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
