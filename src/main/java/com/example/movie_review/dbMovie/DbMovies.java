package com.example.movie_review.dbMovie;

import com.example.movie_review.domain.review.Review;
import com.example.movie_review.movie.MovieDetails;
import com.example.movie_review.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
    @JoinColumn(name = "movieDetails_id")
    private MovieDetails movieDetails;

    @OneToMany(mappedBy = "dbMovies")
    private List<DbRatings> dbRatings;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "dbMovies")
    private List<Review> reviews;
}
