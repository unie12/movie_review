package com.example.movie_review.movie;

import com.example.movie_review.genre.Genres;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name = "movie_genre")
public class MovieGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movieDetails_id")
    private MovieDetails movieDetails;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genres genres;

}
