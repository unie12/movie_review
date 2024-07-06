package com.example.movie_review.kobis;

import lombok.Data;

@Data
public class KobisMovieInfo {
    private MovieInfoResult movieInfoResult;

    @Data
    public static class MovieInfoResult {
        private MovieInfo movieInfo;
    }

    @Data
    public static class MovieInfo {
        private String movieCd;
        private String movieNm;
        private String openDt;
        private String audiAcc;
        // ... 기타 필요한 필드들 ...
    }
}