package com.example.movie_review.favoriteMovie;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Getter @Setter
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
