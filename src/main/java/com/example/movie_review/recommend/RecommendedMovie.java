package com.example.movie_review.recommend;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "tmdb_id")
    private Long tmdbId;

    @Enumerated(EnumType.STRING)
    private RecommendType recommendType;

    private Double score; // 사용자 만족도 점수

    private LocalDateTime createdAt;

}
