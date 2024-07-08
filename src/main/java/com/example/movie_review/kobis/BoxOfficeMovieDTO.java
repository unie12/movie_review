package com.example.movie_review.kobis;

import lombok.Data;

@Data
public class BoxOfficeMovieDTO {
    // 제목 (개봉 나라) 평점 (예매율) 누적 관객
    private String title;
    private String rank;
    private String poster_path;
    private String tmdbId;
    private String audiAcc;

    public BoxOfficeMovieDTO(String title, String rank, String poster_path, String tmdbId, String aduiAcc) {
        this.title = title;
        this.rank = rank;
        this.poster_path = poster_path;
        this.tmdbId = tmdbId;
        this.audiAcc = aduiAcc;
    }
}

