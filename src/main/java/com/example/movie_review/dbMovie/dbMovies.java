//package com.example.movie_review.dbMovie;
//
//import com.example.movie_review.domain.review.Review;
//import com.example.movie_review.movie.MovieDetails;
//import com.example.movie_review.rating.Ratings;
//import com.example.movie_review.user.User;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity @Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "dbMovies")
//public class dbMovies {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "dbMovies_id")
//    private Long id;
//
//    @OneToOne
//    private MovieDetails movieDetails;
//
//    @OneToMany
//    private Ratings ratings;
//
//    @ManyToOne
//    private User user;
//
//    @OneToMany
//    private Review review;
//}
