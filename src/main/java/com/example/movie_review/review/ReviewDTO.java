package com.example.movie_review.review;

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

    public ReviewDTO(Double userRating, int heartCnt, UserCommonDTO user, ReviewCommonDTO review, boolean isLikedByCurrentUser) {
        this.userRating = userRating;
        this.heartCnt = heartCnt;
        this.user = user;
        this.review = review;
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }
}
