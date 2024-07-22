package com.example.movie_review.dbRating;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DbRatings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "DbMovies_id"})
})
public class DbRatings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DbRatings_id")
    private Long id;

    private LocalDateTime uploadRating = LocalDateTime.now();
    private Double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DbMovies_id")
    private DbMovies dbMovies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
