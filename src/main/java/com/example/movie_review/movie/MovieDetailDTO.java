package com.example.movie_review.movie;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MovieDetailDTO {

    private Long id;

    /**
     * 연산 변수들
     */
    private int reviewCnt;
    private int ratingCnt;
    private Double ratingAvg;

    /**
     * 영화 상세 정보
     */
    private String title;
    private String original_title;
    private String backdrop_path;
    private String release_date;
    private List<String> genres = new ArrayList<>();
    private Long runtime;
    private String poster_path;

    private boolean isFavorite;



}
