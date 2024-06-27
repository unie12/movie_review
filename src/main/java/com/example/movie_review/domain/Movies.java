package com.example.movie_review.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NamedEntityGraph(name="Movies.withGenres", attributeNodes = {
        @NamedAttributeNode("genres")
})

@Entity @Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "movies")

public class Movies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @JoinColumn(name = "movie_title")
    private String title;

    @JoinColumn(name = "tmdb_id")
    private Long tId;

    @ManyToMany
    private Set<Genres> genres;

    public MovieData toMovieData() {
        return MovieData.builder().movieId(this.id).tId(this.tId).rating(4.0).build();
    }
}
