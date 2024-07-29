package com.example.movie_review.user.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class WeeklyUserDTO {
    private UserCommonDTO userCommonDTO;
    private Long WeeklyRatingCount;
    private Long WeeklyReviewCount;

    public WeeklyUserDTO(UserCommonDTO userCommonDTO, Long weeklyRatingCount, boolean isRating) {
        this.userCommonDTO = userCommonDTO;
        this.WeeklyRatingCount = weeklyRatingCount;
    }

    public WeeklyUserDTO(UserCommonDTO userCommonDTO, Long weeklyReviewCount) {
        this.userCommonDTO = userCommonDTO;
        this.WeeklyReviewCount = weeklyReviewCount;
    }
}
