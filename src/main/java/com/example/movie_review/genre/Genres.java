package com.example.movie_review.genre;

import com.example.movie_review.movie.MovieDetails;
import com.example.movie_review.movie.MovieGenre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor @NoArgsConstructor
@Table(name = "genres")
public class Genres {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;

    @JoinColumn(name = "genre_name")
    private String name;

//    @Column(name = "genre_original_id")
//    private String genreId;

//    @OneToMany
//    @JoinColumn(name = "genres")
//    private List<MovieGenre> movieGenres;

    @ManyToMany(mappedBy = "genres")
    private Set<MovieDetails> movies = new HashSet<>();


    public Genres(String name) {
        this.name = name;
    }
}
