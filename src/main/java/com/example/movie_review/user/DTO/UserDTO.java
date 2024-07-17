package com.example.movie_review.user.DTO;

import com.example.movie_review.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class UserDTO {
    private Long id;
    private Set<Long> likedReviewIds;

    /**
     * 연산 변수들
     */
    private int favoriteCnt;
    private int reviewCnt;
    private int ratingCnt;
    private int heartCnt;

    private int subscriptionCnt;
    private int subscriberCnt;

    private User user;

}
