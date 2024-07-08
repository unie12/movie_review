package com.example.movie_review.kobis;

import lombok.Data;

import java.util.List;

@Data
public class KobisSearchResult {
    private MovieListResult movieListResult;

    @Data
    public static class MovieListResult {
        private String totCnt;
        private String source;
        private List<Movie> movieList;
    }

    @Data
    public static class Movie {
        private String movieCd;
        private String movieNm;
        private String movieNmEn;
        private String prdtYear;
        private String openDt;
        private String typeNm;
        private String prdtStatNm;
        private String nationAlt;
        private String genreAlt;
        private String repNationNm;
        private String repGenreNm;
        private List<Director> directors;  // String에서 List<Director>로 변경
        private List<Company> companys;    // String에서 List<Company>로 변경
    }

    @Data
    public static class Director {
        private String peopleNm;
    }

    @Data
    public static class Company {
        // 필요한 회사 정보 필드 추가
    }
}