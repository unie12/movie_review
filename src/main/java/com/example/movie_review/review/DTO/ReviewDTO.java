package com.example.movie_review.review.DTO;

import com.example.movie_review.user.DTO.UserCommonDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDTO {
    private Double userRating;
    private int heartCnt;
    private UserCommonDTO user;
    private ReviewCommonDTO review;
    private boolean isLikedByCurrentUser;
    private boolean spoiler;

    public ReviewDTO(Double userRating, int heartCnt, UserCommonDTO user, ReviewCommonDTO review, boolean isLikedByCurrentUser, boolean isSpoiler) {
        this.userRating = userRating;
        this.heartCnt = heartCnt;
        this.user = user;
        this.review = review;
        this.isLikedByCurrentUser = isLikedByCurrentUser;
        this.spoiler = isSpoiler;
    }
}
