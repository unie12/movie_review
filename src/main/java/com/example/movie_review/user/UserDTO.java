package com.example.movie_review.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;

    /**
     * 연산 변수들
     */
    private int favoriteCnt;
    private int reviewCnt;
    private int ratingCnt;
    private int heartCnt;

    private User user;
}
