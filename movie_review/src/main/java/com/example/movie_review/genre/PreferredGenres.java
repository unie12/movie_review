package com.example.movie_review.genre;

import com.example.movie_review.user.User;
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
    private User user;

    private String genreName;
    private Long genreId;

}
