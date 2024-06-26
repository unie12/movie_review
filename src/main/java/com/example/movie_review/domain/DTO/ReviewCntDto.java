package com.example.movie_review.domain.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCntDto {

    private Long totalNoticeCnt;
    private Long totalReviewCnt;
    private Long totalBeforeCnt;
    private Long totalAfterCnt;
    private Long totalFreeCnt;
}
