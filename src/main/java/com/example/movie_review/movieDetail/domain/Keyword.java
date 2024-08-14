package com.example.movie_review.movieDetail.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "keyword")
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    @Column(name = "keyword_name", unique = true)
    private String name;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovieKeyword> movieKeywords = new HashSet<>();

    public void addMovieKeyword(MovieKeyword movieKeyword) {
        movieKeywords.add(movieKeyword);
        movieKeyword.setKeyword(this);
    }

}
