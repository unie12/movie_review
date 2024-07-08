package com.example.movie_review.dbMovie;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class MovieCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MovieType type;

    @Column(columnDefinition = "TEXT")
    private String movieData;

    private LocalDateTime lastUpdated;
}
