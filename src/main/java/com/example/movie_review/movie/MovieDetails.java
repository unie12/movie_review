package com.example.movie_review.movie;

import com.example.movie_review.genre.Genres;
import lombok.Data;

import java.util.List;

@Data
public class MovieDetails {
    private String backdrop_path;
    private Integer id;
    private String title;
    private String original_title;
    private String name;
    private String overview;
    private String tagline;
    private String poster_path;

    private List<Genres> genres;
//    private List<Creator> created_by;
    private String release_date;
    private String first_air_date;
    private double vote_average;
    private Long vote_count;

    private Long runtime;
    private boolean adult;
    private String media_type;

    /**
     * kobis 데이터 결합
     */
    private String rank; // 박스오피스 순위
    private String rankInten; // 전일대비 순위 증감분
    private String rankOldAndNew; // 랭킹 신규진입여부 OLD, NEW
    private String audiCnt; // 해당일 관객수
    private String audiInten; // 전일 대비 관객수 증감분
    private String audiChange; // 전일 대비 관객수 증감 비율
    private String audiAcc; // 누적관객수

    private Credits credits;

    @Data
    public static class Credits {
        private List<Crew> crew;
        private List<Cast> cast;

        @Data
        public static class Crew {
            private boolean adult;
            private Integer gender;
            private Integer id;
            private String known_for_department;
            private String name;
            private String original_name;
            private double popularity;
            private String profile_path;
            private String credit_id;
            private String department;
            private String job;
        }

        @Data
        public static class Cast {
            private boolean adult;
            private Integer gender;
            private Integer id;
            private String known_for_department;
            private String name;
            private String original_name;
            private double popularity;
            private String profile_path;
            private Integer cast_id;
            private String character;
            private String credit_id;
            private Integer order;
        }
    }


}
