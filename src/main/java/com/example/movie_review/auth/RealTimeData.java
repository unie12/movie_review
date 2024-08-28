package com.example.movie_review.auth;

import com.example.movie_review.user.DTO.WeeklyUserDTO;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class RealTimeData {
    private Long userCount;
    private Long totalRatings;
    private Long totalReviews;
    private Map<Double, Long> ratingDistribution;
    private List<WeeklyUserDTO> weeklyRatingKing;
    private List<WeeklyUserDTO> weeklyReviewKing;
    private LocalDateTime nextResetTime;

    public RealTimeData(Long userCount, Long totalRatings, Long totalReviews, Map<Double, Long> ratingDistribution, List<WeeklyUserDTO> weeklyRatingKing, List<WeeklyUserDTO> weeklyReviewKing, LocalDateTime nextResetTime) {
        this.userCount = userCount;
        this.totalRatings = totalRatings;
        this.totalReviews = totalReviews;
        this.ratingDistribution = ratingDistribution;
        this.weeklyRatingKing = weeklyRatingKing;
        this.weeklyReviewKing = weeklyReviewKing;
        this.nextResetTime = nextResetTime;
    }
}