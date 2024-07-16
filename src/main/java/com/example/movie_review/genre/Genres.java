package com.example.movie_review.genre;

import com.example.movie_review.movieDetail.MovieDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor @NoArgsConstructor
@Table(name = "genres")
public class Genres {

    @Id
    @Column(name = "genre_id")
    private Long id;

    @JoinColumn(name = "genre_name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<MovieDetails> movies = new HashSet<>();


    public Genres(String name) {
        this.name = name;
    }
}
