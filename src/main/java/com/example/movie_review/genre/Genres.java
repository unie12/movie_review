package com.example.movie_review.genre;

import com.example.movie_review.movie.MovieDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor @NoArgsConstructor
@Table(name = "genres")
public class Genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;

    @JoinColumn(name = "genre_name")
    private String name;

    @Column(name = "genre_original_id")
    private String genreId;

    @ManyToOne
    @JoinColumn(name = "movieDetails_id")
    private MovieDetails movieDetails;


    public Genres(String name) {
        this.name = name;
    }
}
