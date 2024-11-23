package com.example.movie_review.anonymous;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "anonymous_ratings")
public class AnonymousRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anonymous_rating_id")
    private Long id;

    @Column(nullable = false)
    private Long ratedMovieId;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private LocalDateTime ratedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anonymous_user_id")
    private AnonymousUser anonymousUser;

}
