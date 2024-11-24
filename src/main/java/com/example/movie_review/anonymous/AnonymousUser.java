package com.example.movie_review.anonymous;

import com.example.movie_review.recommend.RecommendedMovie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "anonymous_users")
public class AnonymousUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anonymous_user_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

//    private Double score; // 사용자 만족도 점수

    @OneToMany(mappedBy = "anonymousUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnonymousRating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "anonymousUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendedMovie> recommendedMovies = new ArrayList<>();

}
