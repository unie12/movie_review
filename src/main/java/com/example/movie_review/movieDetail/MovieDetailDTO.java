package com.example.movie_review.movieDetail;

import com.example.movie_review.review.ReviewDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieDetailDTO {
    /**
     * 연산 변수들
     */
    private int reviewCnt;
    private Double ajou_ratingAvg;
    private int ajou_ratingCnt;


    /**
     * 영화 상세 정보
     */
    private MovieBasicInfo basicInfo;

    /**
     * 영화 관련 사용자 정보
     */
    private boolean isFavorite;
    private List<ReviewDTO> reviews;
    private List<Long> userHearts;

}
