package com.example.movie_review.auth;

import com.example.movie_review.user.DTO.WeeklyUserDTO;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class RealTimeData {
    private Long userCount;
    private Long totalRatings;
    private Long totalReviews;
    private Map<Double, Long> ratingDistribution;
    private List<WeeklyUserDTO> weeklyRatingKing;
    private List<WeeklyUserDTO> weeklyReviewKing;

    public RealTimeData(Long userCount, Long totalRatings, Long totalReviews,
                        Map<Double, Long> ratings,
                        List<WeeklyUserDTO> weeklyRatingKing,
                        List<WeeklyUserDTO> weeklyReviewKing) {
        this.userCount = userCount;
        this.totalRatings = totalRatings;
        this.totalReviews = totalReviews;
        this.ratingDistribution = ratings;
        this.weeklyRatingKing = weeklyRatingKing;
        this.weeklyReviewKing = weeklyReviewKing;
    }
}