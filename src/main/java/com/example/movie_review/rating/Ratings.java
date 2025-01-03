package com.example.movie_review.rating;

import com.example.movie_review.movieLens.Movies;
import com.example.movie_review.user.domain.User;
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

    @Column(name = "rating")
    private Double rating;

    @Column(name = "timestamp")
    private Long timestamp;

//    @Transient
    @ManyToOne
    @JoinColumn(name = "movie_id", insertable = false, updatable = false)
    private Movies movie;

}
