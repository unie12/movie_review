package com.example.movie_review.user.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSearchDTO {
    private UserCommonDTO user;
    private boolean isSubscribed;
    private int ratingCnt;
    private int reviewCnt;

    public UserSearchDTO(UserCommonDTO user, boolean isSubscribed, int ratingCnt, int reviewCnt) {
        this.user = user;
        this.isSubscribed = isSubscribed;
        this.ratingCnt = ratingCnt;
        this.reviewCnt = reviewCnt;
    }
}
