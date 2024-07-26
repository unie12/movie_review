package com.example.movie_review.review;

import lombok.Data;

@Data
public class ReviewWithHeartCount {
    private Review review;
    private int heartCount;

    public ReviewWithHeartCount(Review review, int heartCount) {
        this.review = review;
        this.heartCount = heartCount;
    }
}
