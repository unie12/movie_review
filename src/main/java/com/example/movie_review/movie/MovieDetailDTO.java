package com.example.movie_review.movie;

import com.example.movie_review.review.ReviewDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieDetailDTO {

    private Long id;

    /**
     * 연산 변수들
     */
    private int reviewCnt;

    private double tmdb_ratingAvg;
    private Long tmdb_ratingCnt;
    private Double ajou_ratingAvg;
    private int ajou_ratingCnt;


    /**
     * 영화 상세 정보
     */
    private String title;
    private String original_title;
    private String backdrop_path;
    private String release_date;
    private List<String> genres;
    private Long runtime;
    private String poster_path;
    private String tagline;
    private String overview;
    private List<DirectorDTO> directors;
    private List<ActorDTO> actors;

    /**
     * 영화 관련 사용자 정보
     */
    private boolean isFavorite;
    private List<ReviewDTO> reviews;
    private List<Long> userHearts;

}
