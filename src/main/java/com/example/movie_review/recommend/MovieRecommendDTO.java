package com.example.movie_review.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieRecommendDTO {
    private String tmdbId;
    private String title;
    private String poster_path;
    private String popularity;
    private String recommendation_type;
    private String recommendedFrom;      // 추천의 출처가 된 영화 제목
    private Double similarity;           // 유사도 점수
    private String release_date;         // 기존 MovieCommonDTO의 필드들도 포함
    private Long runtime;
    private Double ajou_rating;
    private int ajou_rating_cnt;
}