package com.example.movie_review.review;

import lombok.Data;
import lombok.Getter;

@Getter
public class ReviewDTO {
    private Review review;
    private Double userRating;
    private int heartCnt;

    public ReviewDTO(Review review, Double userRating) {
        this.review = review;
        this.userRating = userRating;
        this.heartCnt = review.getHeartCount();
    }
}
