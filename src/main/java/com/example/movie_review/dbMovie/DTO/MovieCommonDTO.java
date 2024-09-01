package com.example.movie_review.dbMovie.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class MovieCommonDTO {
    private Long id;
    private Integer tId;
    private String title;
    private String poster_path;

    private String release_date;
    private Long runtime;
    private Double ajou_rating;
    private int ajou_rating_cnt;
}
