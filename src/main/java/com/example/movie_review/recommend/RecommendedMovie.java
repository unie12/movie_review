package com.example.movie_review.recommend;

import com.example.movie_review.anonymous.AnonymousUser;
import com.example.movie_review.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recommend_movies")
public class RecommendedMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_movies_id")
    private Long id;

    @Column(nullable = false)
    private Long recommendedMovieId;

    @Column(nullable = false)
    private LocalDateTime recommendedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anonymous_user_id")
    private AnonymousUser anonymousUser;

    @Enumerated(EnumType.STRING)
    private RecommendType recommendType;

    private Double similarity;
}
