package com.example.movie_review.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RealTimeData {
    private Long userCount;
    private Long totalRatings;
    private Long totalReviews;

    public RealTimeData(Long userCount, Long totalRatings, Long totalReviews) {
        this.userCount = userCount;
        this.totalRatings = totalRatings;
        this.totalReviews = totalReviews;
    }
}
