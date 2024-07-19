package com.example.movie_review.review;

import com.example.movie_review.user.DTO.UserCommonDTO;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class ReviewDTO {
    private Double userRating;
    private int heartCnt;
    private UserCommonDTO user;
    private ReviewCommonDTO review;

    public ReviewDTO(Double userRating, int heartCnt, UserCommonDTO user, ReviewCommonDTO review) {
        this.userRating = userRating;
        this.heartCnt = heartCnt;
        this.user = user;
        this.review = review;
    }
}
