package com.example.movie_review.dbMovie;

import com.example.movie_review.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity @Data
@Table(name = "user_favorite_movie")
public class UserFavoriteMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "db_movie_id")
    private DbMovies dbMovie;

    private LocalDateTime favoriteDate;
}
