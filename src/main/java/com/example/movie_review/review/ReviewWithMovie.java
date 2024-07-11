package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.review.Review;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class ReviewWithMovie {
    private Review review;
    private DbMovies dbMovies;
}
