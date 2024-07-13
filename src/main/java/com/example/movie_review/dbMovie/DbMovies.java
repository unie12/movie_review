package com.example.movie_review.dbMovie;

import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.review.Review;
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
    private List<DbRatings> dbRatings = new ArrayList<>();

    @OneToMany(mappedBy = "dbMovie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFavoriteMovie> favoritedByUsers = new ArrayList<>();

    @OneToMany(mappedBy = "dbMovies", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();


    public int getReviewCount() {
//        if(reviews == null)
//                return 0;
        return reviews.size();
    }

    public int getDbRatingCount() {
        return dbRatings.size();
    }

    public double getDbRatingAvg() {
        return dbRatings.stream()
                .mapToDouble(DbRatings::getScore)
                .average()
                .orElse(0.0);
    }


}
