package com.example.movie_review.user.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeeklyUserDTO {
    private UserCommonDTO userCommonDTO;
    private Long weeklyRatingCount;
    private Long weeklyReviewCount;

    public WeeklyUserDTO(UserCommonDTO userCommonDTO, Long weeklyCount, boolean isRating) {
        this.userCommonDTO = userCommonDTO;
        if (isRating) {
            this.weeklyRatingCount = weeklyCount;
        } else {
            this.weeklyReviewCount = weeklyCount;
        }
    }
}
