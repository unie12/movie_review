package com.example.movie_review.genre;

import com.example.movie_review.user.User;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "preferred_genres")
public class PreferredGenres {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preferred_genre_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private String genreName;

    @Column(name = "genre_id")
    private Long genreId;

    public PreferredGenres(User user, Genres genre) {
        this.user = user;
        this.genreId = genre.getId();
        this.genreName = genre.getName();
    }
}
