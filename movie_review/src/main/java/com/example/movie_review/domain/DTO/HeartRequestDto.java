package com.example.movie_review.domain.DTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartRequestDto {

    private Long userId;
    private Long reviewId;

    public HeartRequestDto(Long userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }
}
