package com.example.movie_review.rating;

import com.example.movie_review.movie.Movies;
import com.example.movie_review.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ratings")
public class Ratings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Long ratingId;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "timestamp")
    private Long timestamp;

    @Transient
    @ManyToOne
    @JoinColumn(name = "movie_id", insertable = false, updatable = false)
    private Movies movie;

    @Transient
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

}
